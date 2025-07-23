package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipToDeliveryRequestDto {
    @NotBlank(message = "Order id is required")
    private UUID orderId;

    @NotBlank(message = "Delivery id is required")
    private UUID deliveryId;
}
