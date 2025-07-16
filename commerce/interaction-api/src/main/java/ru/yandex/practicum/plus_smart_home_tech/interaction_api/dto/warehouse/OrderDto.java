package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull(message = "Delivery weight is required")
    private Double deliveryWeight;

    @NotNull(message = "Delivery volume is required")
    private Double deliveryVolume;

    @NotNull(message = "Fragile is required")
    private Boolean fragile;
}
