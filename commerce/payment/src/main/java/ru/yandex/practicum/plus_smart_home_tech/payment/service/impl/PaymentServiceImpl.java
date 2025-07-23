package ru.yandex.practicum.plus_smart_home_tech.payment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderFullDataDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentStatus;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.BadRequestException;
import ru.yandex.practicum.plus_smart_home_tech.payment.dao.PaymentRepository;
import ru.yandex.practicum.plus_smart_home_tech.payment.mapper.PaymentMapper;
import ru.yandex.practicum.plus_smart_home_tech.payment.model.Payment;
import ru.yandex.practicum.plus_smart_home_tech.payment.service.PaymentService;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final double vatRate;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              @Value("${app.payment.vat-rate}") double vatRate) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.vatRate = vatRate;
    }

    @Override
    public PaymentResponseDto addPayment(OrderFullDataDto orderDto) {
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
    public Double getTotalCost(OrderFullDataDto orderDto) {
        return orderDto.getProductPrice() + orderDto.getDeliveryPrice() + orderDto.getProductPrice() * vatRate;
    }

    private void checkPayments(Double... prices) {
        for (Double price : prices) {
            if (price == null || price == 0) {
                throw new BadRequestException("Not enough payment info in order");
            }
        }
    }
}
