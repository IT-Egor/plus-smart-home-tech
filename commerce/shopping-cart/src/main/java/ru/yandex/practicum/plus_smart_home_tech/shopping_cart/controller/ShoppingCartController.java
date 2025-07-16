package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.UpdateProductQuantityRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.ShoppingCartFeign;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartFeign {
    private final ShoppingCartService shoppingCartService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deactivateUserShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateUserShoppingCart(username);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeFromShoppingCart(@RequestParam String username,
                                                  @RequestBody Set<UUID> productIds) {
        return shoppingCartService.removeFromShoppingCart(username, productIds);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @Valid @RequestBody UpdateProductQuantityRequestDto request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }
}
