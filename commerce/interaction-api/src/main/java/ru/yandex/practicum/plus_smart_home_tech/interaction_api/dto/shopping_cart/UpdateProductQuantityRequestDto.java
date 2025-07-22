package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductQuantityRequestDto {
    @NotNull(message = "Product id is required")
    private UUID productId;

    @PositiveOrZero(message = "Product quantity must be positive or zero")
    private Long newQuantity;
}
