package ru.yandex.practicum.plus_smart_home_tech.payment.service;

import jakarta.validation.Valid;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderFullDataDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto addPayment(@Valid OrderFullDataDto orderDto);

    Double getTotalCost(OrderFullDataDto orderDto);
}
