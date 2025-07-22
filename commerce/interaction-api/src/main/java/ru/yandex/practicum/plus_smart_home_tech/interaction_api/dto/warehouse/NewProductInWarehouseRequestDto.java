package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class NewProductInWarehouseRequestDto {
    @NotNull(message = "Product id is required")
    private UUID productId;

    private Boolean fragile;

    @Valid
    @NotNull(message = "Dimension is required")
    private DimensionDto dimension;

    @NotNull(message = "Weight is required")
    @Min(value = 1, message = "Weight must be greater than 0")
    private Double weight;
}
