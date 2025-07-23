package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssembledProductsResponseDto {
    @NotBlank(message = "Delivery weight is required")
    private Double deliveryWeight;

    @NotBlank(message = "Delivery volume is required")
    private Double deliveryVolume;

    @NotBlank(message = "Fragile is required")
    private Boolean fragile;
}
