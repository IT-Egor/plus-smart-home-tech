package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeign {
    @PutMapping
    DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto deliveryDto) throws FeignException;

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID orderId) throws FeignException;

    @PostMapping("/cost")
    Double deliveryCost(@Valid @RequestBody OrderDto orderDto) throws FeignException;
}
