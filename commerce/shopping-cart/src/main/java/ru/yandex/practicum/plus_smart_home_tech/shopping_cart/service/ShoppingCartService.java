package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.UpdateProductQuantityRequestDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(String username);

    ShoppingCartResponseDto addProductToShoppingCart(String username, Map<UUID, Long> products);

    void deactivateUserShoppingCart(String username);

    ShoppingCartResponseDto removeFromShoppingCart(String username, Set<UUID> productIds);

    ShoppingCartResponseDto changeProductQuantity(String username, UpdateProductQuantityRequestDto request);
}
