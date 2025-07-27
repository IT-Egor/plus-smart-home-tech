package ru.yandex.practicum.plus_smart_home_tech.order.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.ReturnProductRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> getClientOrders(String username);

    OrderDto createNewOrder(CreateOrderRequestDto request);

    OrderDto returnProduct(ReturnProductRequestDto request);

    OrderDto payForOrder(UUID orderId);

    OrderDto setFailedPayment(UUID orderId);

    OrderDto setDeliverySuccess(UUID orderId);
}
