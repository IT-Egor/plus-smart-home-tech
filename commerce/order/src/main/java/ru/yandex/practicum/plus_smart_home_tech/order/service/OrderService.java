package ru.yandex.practicum.plus_smart_home_tech.order.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.ReturnProductRequestDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getClientOrders(String username);

    OrderDto createNewOrder(CreateOrderRequestDto request);

    OrderDto returnProduct(ReturnProductRequestDto request);
}
