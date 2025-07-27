package ru.yandex.practicum.plus_smart_home_tech.order.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.order.model.Order;

@Mapper
public interface OrderMapper {
    OrderDto toDto(Order entity);
}
