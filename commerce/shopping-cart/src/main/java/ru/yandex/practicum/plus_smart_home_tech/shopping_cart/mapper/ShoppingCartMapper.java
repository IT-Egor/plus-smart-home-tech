package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.model.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);
}
