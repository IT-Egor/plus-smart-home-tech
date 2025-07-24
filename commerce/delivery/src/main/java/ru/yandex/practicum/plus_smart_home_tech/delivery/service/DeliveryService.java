package ru.yandex.practicum.plus_smart_home_tech.delivery.service;

import jakarta.validation.Valid;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;

public interface DeliveryService {
    DeliveryDto planDelivery(@Valid DeliveryDto deliveryDto);
}
