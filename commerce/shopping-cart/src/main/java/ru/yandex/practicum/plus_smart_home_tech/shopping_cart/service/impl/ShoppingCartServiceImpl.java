package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.UnauthorizedException;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.dao.ShoppingCartRepository;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.model.ShoppingCart;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.ShoppingCartService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getCart(String username) {
        checkUserAuthorization(username);
        return shoppingCartMapper.toResponseDto(
                shoppingCartRepository.findByUsername(username).orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUsername(username);
                    shoppingCart.setIsActive(true);
                    return shoppingCartRepository.save(shoppingCart);
                })
        );
    }

    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new UnauthorizedException("Username must not be blank");
        }
    }
}
