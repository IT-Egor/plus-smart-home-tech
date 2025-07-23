package ru.yandex.practicum.plus_smart_home_tech.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.WarehouseFeign;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.service.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseFeign {
    private final WarehouseService warehouseService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void newProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequestDto request) {
        warehouseService.newProductInWarehouse(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void shipToDelivery(@Valid @RequestBody ShipToDeliveryRequestDto request) {
        warehouseService.shipToDelivery(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public OrderDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCart) {
        return warehouseService.checkProductQuantity(shoppingCart);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequestDto request) {
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public AddressResponseDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
