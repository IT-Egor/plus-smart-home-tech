package ru.yandex.practicum.plus_smart_home_tech.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.AlreadyExistsException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotEnoughProductsInWarehouseException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.dao.OrderBookingRepository;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.dao.WarehouseRepository;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.Dimension;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.OrderBooking;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.service.WarehouseService;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
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
    private final OrderBookingRepository orderBookingRepository;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequestDto request) {
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new AlreadyExistsException("Product `%s` already exists in warehouse".formatted(request.getProductId()));
        }
        warehouseRepository.save(warehouseProductMapper.newProductRequestToEntity(request));
    }

    @Override
    public void shipToDelivery(ShipToDeliveryRequestDto request) {
        OrderBooking orderBooking = findOrderBookingById(request.getOrderId());
        orderBooking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(orderBooking);
    }

    @Override
    public void acceptReturn(Map<UUID, Long> returnedProducts) {
        increaseProductQuantity(returnedProducts);
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

    @Override
    public OrderDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        ShoppingCartDto shoppingCart = ShoppingCartDto.builder()
                .shoppingCartId(request.getOrderId())
                .products(request.getProducts())
                .build();
        OrderDto order = checkProductQuantity(shoppingCart);

        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setOrderId(request.getOrderId());
        orderBooking.setProducts(request.getProducts());
        orderBookingRepository.save(orderBooking);

        decreaseProductQuantity(request.getProducts());
        return order;
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequestDto request) {
        WarehouseProduct product = findProductById(request.getProductId());
        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
    }

    @Override
    public AddressResponseDto getWarehouseAddress() {
        String address = CURRENT_ADDRESS;
        return AddressResponseDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();
    }

    private void checkProductsAvailability(Set<UUID> productIds, Map<UUID, WarehouseProduct> products) {
        for (UUID productId : productIds) {
            if (!products.containsKey(productId)) {
                throw new NotFoundException("Product with id `%s` not found".formatted(productId));
            }
        }
    }

    private WarehouseProduct findProductById(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with id `%s` not found".formatted(productId)));
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

    private OrderBooking findOrderBookingById(UUID orderId) {
        return orderBookingRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id `%s` not found".formatted(orderId)));
    }

    private void increaseProductQuantity(Map<UUID, Long> returnedProducts) {
        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(returnedProducts.keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        for (Map.Entry<UUID, Long> entry : returnedProducts.entrySet()) {
            if (products.containsKey(entry.getKey())) {
                WarehouseProduct product = products.get(entry.getKey());
                product.setQuantity(product.getQuantity() + entry.getValue());
            }
        }
        warehouseRepository.saveAll(products.values());
    }

    private void decreaseProductQuantity(Map<UUID, Long> orderedProducts) {
        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(orderedProducts.keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        for (Map.Entry<UUID, Long> entry : orderedProducts.entrySet()) {
            if (products.containsKey(entry.getKey())) {
                WarehouseProduct product = products.get(entry.getKey());
                if (product.getQuantity() < entry.getValue()) {
                    throw new NotEnoughProductsInWarehouseException(
                            "Product `%s` quantity is not enough".formatted(product.getProductId()));
                }
                product.setQuantity(product.getQuantity() - entry.getValue());
            }
        }
        warehouseRepository.saveAll(products.values());
    }
}
