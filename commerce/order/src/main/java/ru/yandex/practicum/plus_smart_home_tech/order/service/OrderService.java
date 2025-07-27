package ru.yandex.practicum.plus_smart_home_tech.order.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getClientOrders(String username);
}
