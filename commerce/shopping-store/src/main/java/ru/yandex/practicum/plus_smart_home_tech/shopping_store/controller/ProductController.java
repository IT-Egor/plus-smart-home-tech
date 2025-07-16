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
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.ProductService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return productService.getProductsByCategory(category, pageable);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.removeProductFromStore(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@Valid SetProductQuantityStateRequestDto request) {
        return productService.setProductQuantityState(request);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    ProductDto getProduct(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }
}
