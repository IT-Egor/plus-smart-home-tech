package ru.yandex.practicum.plus_smart_home_tech.shopping_cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.UpdateProductQuantityRequestDto;
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
    ShoppingCartResponseDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartResponseDto addProductToShoppingCart(@RequestParam String username,
                                                     @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void deactivateUserShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateUserShoppingCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartResponseDto removeFromShoppingCart(@RequestParam String username,
                                                   @RequestBody Set<UUID> productIds) {
        return shoppingCartService.removeFromShoppingCart(username, productIds);
    }

    @PostMapping("/change-quantity")
    ShoppingCartResponseDto changeProductQuantity(@RequestParam String username,
                                                  @Valid @RequestBody UpdateProductQuantityRequestDto request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }
}
