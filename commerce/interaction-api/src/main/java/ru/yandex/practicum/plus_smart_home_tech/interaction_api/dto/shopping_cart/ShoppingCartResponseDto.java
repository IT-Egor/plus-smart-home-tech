package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponseDto {
    @NotNull
    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Long> products;
}
