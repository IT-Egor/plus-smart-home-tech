package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.UpdateProductQuantityRequestDto;
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
    public ShoppingCartDto getShoppingCart(String username) {
        checkUserAuthorization(username);
        return shoppingCartMapper.toDto(
                shoppingCartRepository.findByUsername(username).orElseGet(() ->
                        shoppingCartRepository.save(createCart(username, new HashMap<>()))
                )
        );
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        checkUserAuthorization(username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .map(existingCart -> updateCartProducts(existingCart, products))
                .orElseGet(() -> createCart(username, products));

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deactivateUserShoppingCart(String username) {
        checkUserAuthorization(username);
        shoppingCartRepository.findByUsername(username)
                .ifPresent(cart -> {
                    cart.setIsActive(false);
                    shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, Set<UUID> productIds) {
        checkUserAuthorization(username);
        ShoppingCart shoppingCart = findCartByUsername(username);
        for (UUID productId : productIds) {
            shoppingCart.getProducts().remove(productId);
        }
        return shoppingCartMapper.toDto((shoppingCartRepository.save(shoppingCart)));
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, UpdateProductQuantityRequestDto request) {
        checkUserAuthorization(username);
        ShoppingCart shoppingCart = findCartByUsername(username);
        if (!shoppingCart.getProducts().containsKey(request.getProductId())) {
            throw new NotFoundException("Product `%s` not found in cart".formatted(request.getProductId()));
        }
        shoppingCart.getProducts().put(request.getProductId(), request.getNewQuantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
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

    private ShoppingCart updateCartProducts(ShoppingCart shoppingCart, Map<UUID, Long> newProducts) {
        newProducts.forEach((productId, quantity) ->
                shoppingCart.getProducts().merge(productId, quantity, Long::sum));
        return shoppingCart;
    }
}
