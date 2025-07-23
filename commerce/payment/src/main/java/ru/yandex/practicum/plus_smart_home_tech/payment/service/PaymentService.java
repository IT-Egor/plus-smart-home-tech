package ru.yandex.practicum.plus_smart_home_tech.payment.service;

import jakarta.validation.Valid;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto addPayment(@Valid OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    void processSuccessPayment(UUID paymentId);
}
