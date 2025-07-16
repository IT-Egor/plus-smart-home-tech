package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {
    @NotNull(message = "Width is required")
    @Min(value = 1, message = "Width must be greater than 0")
    private Double width;

    @NotNull(message = "Height is required")
    @Min(value = 1, message = "Height must be greater than 0")
    private Double height;

    @NotNull(message = "Depth is required")
    @Min(value = 1, message = "Depth must be greater than 0")
    private Double depth;
}
