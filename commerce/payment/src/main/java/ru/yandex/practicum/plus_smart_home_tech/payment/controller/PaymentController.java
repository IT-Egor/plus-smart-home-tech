package ru.yandex.practicum.plus_smart_home_tech.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.OrderFullDataDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.PaymentFeign;
import ru.yandex.practicum.plus_smart_home_tech.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentFeign {
    private final PaymentService paymentService;

    @Override
    public PaymentResponseDto addPayment(@Valid @RequestBody OrderFullDataDto orderDto) {
        return paymentService.addPayment(orderDto);
    }

    @Override
    public Double getTotalCost(@Valid @RequestBody OrderFullDataDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }
}
