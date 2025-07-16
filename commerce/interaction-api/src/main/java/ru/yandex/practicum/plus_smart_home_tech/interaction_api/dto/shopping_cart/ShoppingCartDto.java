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
public class ShoppingCartDto {
    @NotNull(message = "Shopping cart id is required")
    private UUID shoppingCartId;

    @NotNull(message = "Products in shopping cart is required")
    private Map<UUID, Long> products;
}
