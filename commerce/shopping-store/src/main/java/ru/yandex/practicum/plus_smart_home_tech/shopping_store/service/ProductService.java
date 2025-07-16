package ru.yandex.practicum.plus_smart_home_tech.shopping_store.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;

import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto addProduct(@Valid ProductDto productDto);

    ProductDto updateProduct(@Valid ProductDto productDto);

    Boolean removeProduct(UUID productId);

    ProductDto getProductById(UUID productId);
}
