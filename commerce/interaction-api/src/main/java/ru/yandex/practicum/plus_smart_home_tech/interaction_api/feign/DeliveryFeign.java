package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeign {
    @PutMapping
    DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto deliveryDto) throws FeignException;
}
