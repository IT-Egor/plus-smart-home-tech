package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.AddressDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    @Valid
    @NotNull(message = "Shopping cart is required")
    private ShoppingCartDto shoppingCart;

    @Valid
    @NotNull(message = "Delivery address is required")
    private AddressDto deliveryAddress;
}
