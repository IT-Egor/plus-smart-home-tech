package ru.yandex.practicum.plus_smart_home_tech.warehouse.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.WarehouseProduct;

@Mapper
public interface WarehouseProductMapper {
    WarehouseProduct newProductRequestToEntity(NewProductInWarehouseRequestDto request);
}
