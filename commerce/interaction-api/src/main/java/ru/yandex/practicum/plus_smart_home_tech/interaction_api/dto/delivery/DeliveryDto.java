package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotNull(message = "Delivery id is required")
    private UUID deliveryId;

    @NotNull(message = "From address is required")
    private AddressDto fromAddress;

    @NotNull(message = "To address is required")
    private AddressDto toAddress;

    @NotNull(message = "Order id is required")
    private UUID orderId;

    @NotNull(message = "Delivery state is required")
    private DeliveryState deliveryState;
}
