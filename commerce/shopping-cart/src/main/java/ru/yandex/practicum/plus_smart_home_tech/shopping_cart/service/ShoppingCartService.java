package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;

import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(String username);

    ShoppingCartResponseDto addProductToCart(String username, Map<UUID, Long> products);

    void deleteUserCart(String username);
}
