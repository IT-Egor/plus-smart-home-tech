package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderFullDataDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeign {
    @PostMapping
    PaymentResponseDto addPayment(@Valid @RequestBody OrderFullDataDto orderDto) throws FeignException;

    @PostMapping("/totalCost")
    Double getTotalCost(@Valid @RequestBody OrderFullDataDto orderDto) throws FeignException;
}
