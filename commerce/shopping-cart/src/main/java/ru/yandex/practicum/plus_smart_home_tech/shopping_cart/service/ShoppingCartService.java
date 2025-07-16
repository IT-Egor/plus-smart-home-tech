package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(String username);
}
