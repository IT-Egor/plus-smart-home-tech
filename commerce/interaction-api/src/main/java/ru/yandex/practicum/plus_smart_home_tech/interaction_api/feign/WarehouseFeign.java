package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.*;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeign {
    @PutMapping
    void newProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequestDto request) throws FeignException;

    @PostMapping("/shipped")
    void shipToDelivery(@Valid @RequestBody ShipToDeliveryRequestDto request) throws FeignException;

    @PostMapping("/check")
    OrderDto checkProductQuantity(@Valid @RequestBody ShoppingCartDto shoppingCart) throws FeignException;

    @PostMapping("/add")
    void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequestDto request) throws FeignException;

    @GetMapping("/address")
    AddressResponseDto getWarehouseAddress() throws FeignException;
}
