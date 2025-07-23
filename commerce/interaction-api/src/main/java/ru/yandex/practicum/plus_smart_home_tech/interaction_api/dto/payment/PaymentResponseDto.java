package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private UUID paymentId;
    private Double totalPayment;
    private Double deliveryTotal;
    private Double vatTotal;
}
