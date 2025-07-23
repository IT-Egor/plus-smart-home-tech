package ru.yandex.practicum.plus_smart_home_tech.payment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentStatus;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.BadRequestException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.OrderFeign;
import ru.yandex.practicum.plus_smart_home_tech.payment.dao.PaymentRepository;
import ru.yandex.practicum.plus_smart_home_tech.payment.mapper.PaymentMapper;
import ru.yandex.practicum.plus_smart_home_tech.payment.model.Payment;
import ru.yandex.practicum.plus_smart_home_tech.payment.service.PaymentService;

import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final double vatRate;
    private final OrderFeign orderFeign;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              @Value("${app.payment.vat-rate}") double vatRate,
                              OrderFeign orderFeign) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.vatRate = vatRate;
        this.orderFeign = orderFeign;
    }

    @Override
    public PaymentResponseDto addPayment(OrderDto orderDto) {
        checkPayments(orderDto.getProductPrice(), orderDto.getDeliveryPrice(), orderDto.getTotalPrice());
        Payment payment = new Payment();
        payment.setOrderId(orderDto.getOrderId());
        payment.setTotalPayment(getTotalCost(orderDto));
        payment.setDeliveryTotal(orderDto.getDeliveryPrice());
        payment.setVatTotal(orderDto.getTotalPrice() * vatRate);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        return paymentMapper.toResponseDto(paymentRepository.save(payment));
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        return orderDto.getProductPrice() + orderDto.getDeliveryPrice() + orderDto.getProductPrice() * vatRate;
    }

    @Override
    public void processSuccessPayment(UUID paymentId) {
        Payment payment = findPaymentById(paymentId);
        orderFeign.payment(payment.getOrderId());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    private Payment findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment `%s` not found".formatted(paymentId)));
    }

    private void checkPayments(Double... prices) {
        for (Double price : prices) {
            if (price == null || price == 0) {
                throw new BadRequestException("Not enough payment info in order");
            }
        }
    }
}
