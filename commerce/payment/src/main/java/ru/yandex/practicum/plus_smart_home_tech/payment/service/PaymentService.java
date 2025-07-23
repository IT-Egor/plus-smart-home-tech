package ru.yandex.practicum.plus_smart_home_tech.payment.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto addPayment(OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    void processSuccessPayment(UUID paymentId);

    Double calculateProductsCost(OrderDto orderDto);

    void processFailedPayment(UUID paymentId);
}
