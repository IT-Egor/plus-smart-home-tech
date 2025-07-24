package ru.yandex.practicum.plus_smart_home_tech.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.plus_smart_home_tech.delivery.service.DeliveryService;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.DeliveryFeign;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryFeign {
    private final DeliveryService deliveryService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deliverySuccessful(@RequestBody UUID orderId) {
        deliveryService.setSuccessful(orderId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deliveryPicked(@RequestBody UUID orderId) {
        deliveryService.setPicked(orderId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deliveryFailed(@RequestBody UUID orderId) {
        deliveryService.setFailed(orderId);
    }
}
