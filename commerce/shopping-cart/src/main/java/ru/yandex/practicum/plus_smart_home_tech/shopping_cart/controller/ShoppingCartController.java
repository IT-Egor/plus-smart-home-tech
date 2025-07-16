package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.shopping_cart.service.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartResponseDto getCart(@RequestParam String username) {
        return shoppingCartService.getCart(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartResponseDto addProductToCart(@RequestParam String username,
                                             @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductToCart(username, products);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void deactivateUserCart(@RequestParam String username) {
        shoppingCartService.deleteUserCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartResponseDto removeFromCart(@RequestParam String username,
                                           @RequestBody Set<UUID> productIds) {
        return shoppingCartService.removeFromCart(username, productIds);
    }
}
