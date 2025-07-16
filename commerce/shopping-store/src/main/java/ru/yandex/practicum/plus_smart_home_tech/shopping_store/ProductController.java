package ru.yandex.practicum.plus_smart_home_tech.shopping_store;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Page<ProductDto> productDtos = productService.getProductsByCategory(category, pageable);
        return new PagedModel<>(productDtos);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }
}
