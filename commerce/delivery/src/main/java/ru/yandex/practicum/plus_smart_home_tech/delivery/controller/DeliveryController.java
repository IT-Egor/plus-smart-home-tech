package ru.yandex.practicum.plus_smart_home_tech.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    public void deliverySuccessful(@RequestBody UUID orderId) {
        deliveryService.setSuccessful(orderId);
    }
}
