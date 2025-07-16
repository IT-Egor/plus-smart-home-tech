package ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.mapper.ProductMapper;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.dao.ProductRepository;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.model.Product;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.service.ProductService;

import java.util.UUID;

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

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        findProductById(productDto.getProductId());
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        Product product = findProductById(productId);
        if (product.getProductState().equals(ProductState.DEACTIVATE)) {
            return false;
        }
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequestDto request) {
        Product product = findProductById(request.getProductId());
        if (product.getQuantityState().equals(request.getQuantityState())) {
            return false;
        }
        product.setQuantityState(request.getQuantityState());
        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.toDto(findProductById(productId));
    }

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with id `%s` not found".formatted(productId)));
    }
}
