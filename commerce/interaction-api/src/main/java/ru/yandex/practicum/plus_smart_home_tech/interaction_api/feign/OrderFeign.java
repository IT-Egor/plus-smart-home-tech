package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeign {
    @GetMapping
    List<OrderDto> getClientOrders(@RequestParam String username) throws FeignException;

    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/completed")
    OrderDto complete(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId) throws FeignException;
}
