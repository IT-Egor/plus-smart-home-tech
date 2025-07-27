package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeign {
    @PostMapping
    PaymentResponseDto addPayment(@Valid @RequestBody OrderDto orderDto) throws FeignException;

    @PostMapping("/totalCost")
    Double getTotalCost(@Valid @RequestBody OrderDto orderDto) throws FeignException;

    @PostMapping("/refund")
    void paymentSuccess(@RequestBody UUID paymentId) throws FeignException;

    @PostMapping("/productCost")
    Double productCost(@Valid @RequestBody OrderDto orderDto) throws FeignException;

    @PostMapping("/failed")
    void paymentFailed(@RequestBody UUID paymentId) throws FeignException;
}
