package ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.ProductMapper;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.ProductRepository;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.model.Product;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.ProductService;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        return productRepository.findAllByProductCategory(category, pageable)
                .map(productMapper::toDto);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        return productMapper.toDto(productRepository.save(product));
    }
}
