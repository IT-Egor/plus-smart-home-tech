package ru.yandex.practicum.plus_smart_home_tech.warehouse.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddressResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDto;

public interface WarehouseService {
    void newProductInWarehouse(NewProductInWarehouseRequestDto request);

    OrderDto checkProductQuantity(ShoppingCartDto shoppingCart);

    void addProductToWarehouse(AddProductToWarehouseRequestDto request);

    AddressResponseDto getWarehouseAddress();
}
