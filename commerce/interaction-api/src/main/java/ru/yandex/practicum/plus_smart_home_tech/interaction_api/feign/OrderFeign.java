package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeign {
    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId) throws FeignException;
}
