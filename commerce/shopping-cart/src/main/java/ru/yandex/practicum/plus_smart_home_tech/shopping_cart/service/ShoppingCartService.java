package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.UpdateProductQuantityRequestDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products);

    void deactivateUserShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, Set<UUID> productIds);

    ShoppingCartDto changeProductQuantity(String username, UpdateProductQuantityRequestDto request);
}
