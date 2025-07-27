package ru.yandex.practicum.plus_smart_home_tech.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.ReturnProductRequestDto;
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
    public OrderDto createNewOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        return orderService.createNewOrder(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto productReturn(@Valid @RequestBody ReturnProductRequestDto request) {
        return orderService.returnProduct(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto payment(@RequestBody UUID orderId) {
        return orderService.payForOrder(orderId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return orderService.setFailedPayment(orderId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody UUID orderId) {
        return orderService.setDeliverySuccess(orderId);
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
