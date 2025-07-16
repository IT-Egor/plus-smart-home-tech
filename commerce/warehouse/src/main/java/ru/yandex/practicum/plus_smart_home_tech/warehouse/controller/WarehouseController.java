package ru.yandex.practicum.plus_smart_home_tech.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddressResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.service.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequestDto request) {
        warehouseService.newProductInWarehouse(request);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    OrderDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCart) {
        return warehouseService.checkProductQuantity(shoppingCart);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequestDto request) {
        warehouseService.addProductToWarehouse(request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    AddressResponseDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
