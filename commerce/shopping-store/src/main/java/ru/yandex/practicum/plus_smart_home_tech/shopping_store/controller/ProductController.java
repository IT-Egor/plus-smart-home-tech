package ru.yandex.practicum.plus_smart_home_tech.shopping_store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.StoreFeign;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.ProductService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ProductController implements StoreFeign {
    private final ProductService productService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return productService.getProductsByCategory(category, pageable);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.removeProductFromStore(productId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Boolean setProductQuantityState(@Valid SetProductQuantityStateRequestDto request) {
        return productService.setProductQuantityState(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }
}
