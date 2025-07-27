package ru.yandex.practicum.plus_smart_home_tech.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.OrderFeign;
import ru.yandex.practicum.plus_smart_home_tech.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderFeign {
    private final OrderService orderService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getClientOrders(@RequestParam String username) {
        return orderService.getClientOrders(username);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto payment(@RequestBody UUID orderId) {
        return null;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return null;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto complete(@RequestBody UUID orderId) {
        return null;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assembly(@RequestBody UUID orderId) {
        return null;
    }
}
