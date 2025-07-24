package ru.yandex.practicum.plus_smart_home_tech.delivery.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.delivery.model.Delivery;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;

@Mapper
public interface DeliveryMapper {
    Delivery toEntity(DeliveryDto deliveryDto);

    DeliveryDto toDto(Delivery delivery);
}