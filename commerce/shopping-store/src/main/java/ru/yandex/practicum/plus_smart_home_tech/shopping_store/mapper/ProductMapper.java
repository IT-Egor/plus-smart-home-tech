package ru.yandex.practicum.plus_smart_home_tech.shopping_store.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.ProductDto;
import ru.yandex.practicum.plus_smart_home_tech.shopping_store.model.Product;

@Mapper
public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
