package ru.yandex.practicum.plus_smart_home_tech.payment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.payment.PaymentStatus;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.BadRequestException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.OrderFeign;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.StoreFeign;
import ru.yandex.practicum.plus_smart_home_tech.payment.dao.PaymentRepository;
import ru.yandex.practicum.plus_smart_home_tech.payment.mapper.PaymentMapper;
import ru.yandex.practicum.plus_smart_home_tech.payment.model.Payment;
import ru.yandex.practicum.plus_smart_home_tech.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BigDecimal vatRate;
    private final OrderFeign orderFeign;
    private final StoreFeign storeFeign;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              @Value("${app.payment.vat-rate}") BigDecimal vatRate,
                              OrderFeign orderFeign,
                              StoreFeign storeFeign) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.vatRate = vatRate;
        this.orderFeign = orderFeign;
        this.storeFeign = storeFeign;
    }

    @Override
    public PaymentResponseDto addPayment(OrderDto orderDto) {
        checkPayments(orderDto.getProductPrice(), orderDto.getDeliveryPrice(), orderDto.getTotalPrice());
        Payment payment = new Payment();
        payment.setOrderId(orderDto.getOrderId());
        payment.setTotalPayment(getTotalCost(orderDto));
        payment.setDeliveryTotal(orderDto.getDeliveryPrice());
        payment.setVatTotal(orderDto.getTotalPrice().multiply(vatRate));
        payment.setPaymentStatus(PaymentStatus.PENDING);

        return paymentMapper.toResponseDto(paymentRepository.save(payment));
    }

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        BigDecimal productPrice = orderDto.getProductPrice();
        BigDecimal deliveryPrice = orderDto.getDeliveryPrice();
        BigDecimal vat = productPrice.multiply(vatRate);

        return productPrice
                .add(deliveryPrice)
                .add(vat);
    }

    @Override
    public void processSuccessPayment(UUID paymentId) {
        Payment payment = findPaymentById(paymentId);
        orderFeign.payment(payment.getOrderId());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Override
    public BigDecimal calculateProductsCost(OrderDto orderDto) {
        Map<UUID, Long> productQuantities = orderDto.getProducts();

        Map<UUID, BigDecimal> productPrices = productQuantities.keySet().stream()
                .map(storeFeign::getProduct)
                .collect(Collectors.toMap(ProductDto::getProductId, ProductDto::getPrice));

        return productQuantities.entrySet().stream()
                .map(entry -> {
                    BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
                    BigDecimal price = productPrices.get(entry.getKey());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void processFailedPayment(UUID paymentId) {
        Payment payment = findPaymentById(paymentId);
        orderFeign.paymentFailed(payment.getOrderId());
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    private Payment findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment `%s` not found".formatted(paymentId)));
    }

    private void checkPayments(BigDecimal... prices) {
        for (BigDecimal price : prices) {
            if (price == null || price.doubleValue() <= 0) {
                throw new BadRequestException("Not enough payment info in order");
            }
        }
    }
}
