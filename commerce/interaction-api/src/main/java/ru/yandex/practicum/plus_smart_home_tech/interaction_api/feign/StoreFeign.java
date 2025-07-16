package ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreFeign {
    @GetMapping
    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) throws FeignException;

    @PutMapping
    ProductDto createNewProduct(@Valid @RequestBody ProductDto productDto) throws FeignException;

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) throws FeignException;

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId) throws FeignException;

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@Valid SetProductQuantityStateRequestDto request) throws FeignException;

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId) throws FeignException;
}
