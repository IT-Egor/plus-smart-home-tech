package ru.yandex.practicum.plus_smart_home_tech.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.PaymentFeign;
import ru.yandex.practicum.plus_smart_home_tech.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentFeign {
    private final PaymentService paymentService;

    @Override
    public PaymentResponseDto addPayment(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.addPayment(orderDto);
    }

    @Override
    public Double getTotalCost(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    public void paymentSuccess(@RequestBody UUID paymentId) {
        paymentService.processSuccessPayment(paymentId);
    }

    @Override
    public Double productCost(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.calculateProductsCost(orderDto);
    }

    @Override
    public void paymentFailed(@RequestBody UUID paymentId) {
        paymentService.processFailedPayment(paymentId);
    }
}
