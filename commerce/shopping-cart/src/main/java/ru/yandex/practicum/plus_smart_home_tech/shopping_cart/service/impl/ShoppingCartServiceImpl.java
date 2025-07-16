package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.UnauthorizedException;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.dao.ShoppingCartRepository;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.model.ShoppingCart;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.ShoppingCartService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
                shoppingCartRepository.findByUsername(username).orElseGet(() ->
                        shoppingCartRepository.save(createCart(username, new HashMap<>()))
                )
        );
    }

    @Override
    public ShoppingCartResponseDto addProductToCart(String username, Map<UUID, Long> products) {
        checkUserAuthorization(username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .map(existingCart -> updateCartProducts(existingCart, products))
                .orElseGet(() -> createCart(username, products));

        return shoppingCartMapper.toResponseDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteUserCart(String username) {
        checkUserAuthorization(username);
        shoppingCartRepository.findByUsername(username)
                .ifPresent(cart -> {
                    cart.setIsActive(false);
                    shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCartResponseDto removeFromCart(String username, Set<UUID> productIds) {
        checkUserAuthorization(username);
        ShoppingCart cart = findCartByUsername(username);
        for (UUID productId : productIds) {
            cart.getProducts().remove(productId);
        }
        return shoppingCartMapper.toResponseDto((shoppingCartRepository.save(cart)));
    }

    private ShoppingCart findCartByUsername(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User `%s` cart not found".formatted(username)));
    }

    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new UnauthorizedException("Username must not be blank");
        }
    }

    private ShoppingCart createCart(String username, Map<UUID, Long> products) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUsername(username);
        shoppingCart.setProducts(products);
        shoppingCart.setIsActive(true);
        return shoppingCart;
    }

    private ShoppingCart updateCartProducts(ShoppingCart cart, Map<UUID, Long> newProducts) {
        newProducts.forEach((productId, quantity) ->
                cart.getProducts().merge(productId, quantity, Long::sum));
        return cart;
    }
}
