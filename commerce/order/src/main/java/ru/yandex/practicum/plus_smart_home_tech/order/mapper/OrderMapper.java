package ru.yandex.practicum.plus_smart_home_tech.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDataDto;
import ru.yandex.practicum.plus_smart_home_tech.order.model.Order;

@Mapper
public interface OrderMapper {
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "shoppingCartId", source = "request.shoppingCart.shoppingCartId")
    @Mapping(target = "products", source = "request.shoppingCart.products")
    @Mapping(target = "state", constant = "NEW")
    @Mapping(target = "deliveryWeight", source = "orderDataDto.deliveryWeight")
    @Mapping(target = "deliveryVolume", source = "orderDataDto.deliveryVolume")
    @Mapping(target = "fragile", source = "orderDataDto.fragile")
    Order createRequestToOrder(CreateOrderRequestDto request, OrderDataDto orderDataDto);

    OrderDto toDto(Order entity);
}
