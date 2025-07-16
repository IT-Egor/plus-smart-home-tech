package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.AddressResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDto;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeign {
    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequestDto request) throws FeignException;

    @PostMapping("/check")
    OrderDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCart) throws FeignException;

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequestDto request) throws FeignException;

    @GetMapping("/address")
    AddressResponseDto getWarehouseAddress() throws FeignException;
}
