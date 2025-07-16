package ru.yandex.practicum.plus_smart_home_tech.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.NewProductInWarehouseRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.AlreadyExistsException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotEnoughProductsInWarehouseException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.dao.WarehouseRepository;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.Dimension;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductMapper warehouseProductMapper;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequestDto request) {
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new AlreadyExistsException("Product `%s` already exists in warehouse".formatted(request.getProductId()));
        }
        warehouseRepository.save(warehouseProductMapper.newProductRequestToEntity(request));
    }

    @Override
    public OrderDto checkProductQuantity(ShoppingCartDto shoppingCart) {
        Set<UUID> productIds = shoppingCart.getProducts().keySet();

        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        checkProductsAvailability(productIds, products);

        return createOrderDto(shoppingCart, products);
    }

    private void checkProductsAvailability(Set<UUID> productIds, Map<UUID, WarehouseProduct> products) {
        for (UUID productId : productIds) {
            if (!products.containsKey(productId)) {
                throw new NotFoundException("Product with id `%s` not found".formatted(productId));
            }
        }
    }

    private OrderDto createOrderDto(ShoppingCartDto shoppingCart, Map<UUID, WarehouseProduct> products) {
        boolean hasFragile = false;
        double totalVolume = 0;
        double totalWeight = 0;

        for (Map.Entry<UUID, Long> entry : shoppingCart.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Long requestedQuantity = entry.getValue();
            WarehouseProduct product = products.get(productId);

            if (product.getQuantity() < requestedQuantity) {
                throw new NotEnoughProductsInWarehouseException(
                        "Product `%s` quantity is not enough".formatted(productId));
            }

            if (product.isFragile()) {
                hasFragile = true;
            }

            double productVolume = calculateVolume(product.getDimension());
            totalVolume += productVolume * requestedQuantity;
            totalWeight += product.getWeight() * requestedQuantity;
        }

        return OrderDto.builder()
                .fragile(hasFragile)
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .build();
    }

    private double calculateVolume(Dimension dimension) {
        return dimension.getWidth() * dimension.getHeight() * dimension.getDepth();
    }
}
