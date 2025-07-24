package ru.yandex.practicum.plus_smart_home_tech.delivery.service;

import jakarta.validation.Valid;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(@Valid DeliveryDto deliveryDto);

    void setSuccessful(UUID orderId);

    void setPicked(UUID orderId);
}
