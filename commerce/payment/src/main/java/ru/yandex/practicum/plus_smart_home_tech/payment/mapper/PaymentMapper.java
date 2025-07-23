package ru.yandex.practicum.plus_smart_home_tech.payment.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.payment.model.Payment;

@Mapper
public interface PaymentMapper {
    PaymentResponseDto toResponseDto(Payment payment);
}
