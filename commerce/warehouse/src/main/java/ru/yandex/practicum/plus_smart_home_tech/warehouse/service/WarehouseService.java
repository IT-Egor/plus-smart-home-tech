package ru.yandex.practicum.plus_smart_home_tech.warehouse.service;

import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.AddressDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void newProductInWarehouse(NewProductInWarehouseRequestDto request);

    void shipToDelivery(ShipToDeliveryRequestDto request);

    void acceptReturn(Map<UUID, Long> returnedProducts);

    OrderDataDto checkProductQuantity(ShoppingCartDto shoppingCart);

    OrderDataDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);

    void addProductToWarehouse(AddProductToWarehouseRequestDto request);

    AddressDto getWarehouseAddress();
}
