package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.ReturnProductRequestDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeign {
    @GetMapping
    List<OrderDto> getClientOrders(@RequestParam String username) throws FeignException;

    @PutMapping
    OrderDto createNewOrder(@Valid @RequestBody CreateOrderRequestDto request) throws FeignException;

    @PostMapping("/return")
    OrderDto productReturn(@Valid @RequestBody ReturnProductRequestDto request) throws FeignException;

    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/completed")
    OrderDto complete(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId) throws FeignException;
}
