package ru.yandex.practicum.plus_smart_home_tech.payment.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto addPayment(OrderDto orderDto);

    BigDecimal getTotalCost(OrderDto orderDto);

    void processSuccessPayment(UUID paymentId);

    BigDecimal calculateProductsCost(OrderDto orderDto);

    void processFailedPayment(UUID paymentId);
}
