package ru.yandex.practicum.plus_smart_home_tech.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.AddressDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.CreateOrderRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.ReturnProductRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.payment.PaymentResponseDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.OrderDataDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.delivery.DeliveryState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.order.OrderState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.UnauthorizedException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.DeliveryFeign;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.PaymentFeign;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.ShoppingCartFeign;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.WarehouseFeign;
import ru.yandex.practicum.plus_smart_home_tech.order.dao.OrderRepository;
import ru.yandex.practicum.plus_smart_home_tech.order.mapper.OrderMapper;
import ru.yandex.practicum.plus_smart_home_tech.order.model.Order;
import ru.yandex.practicum.plus_smart_home_tech.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartFeign shoppingCartFeign;
    private final WarehouseFeign warehouseFeign;
    private final DeliveryFeign deliveryFeign;
    private final PaymentFeign paymentFeign;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        checkUserAuthorization(username);
        ShoppingCartDto shoppingCartDto = shoppingCartFeign.getShoppingCart(username);
        return orderRepository.findByShoppingCartId(shoppingCartDto.getShoppingCartId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(CreateOrderRequestDto request) {
        OrderDataDto orderDataDto = warehouseFeign.checkProductQuantity(request.getShoppingCart());
        Order order = orderMapper.createRequestToOrder(request, orderDataDto);
        order = orderRepository.save(order);

        AddressDto warehouseAddress = warehouseFeign.getWarehouseAddress();
        DeliveryDto newDelivery = DeliveryDto.builder()
                .fromAddress(warehouseAddress)
                .toAddress(request.getDeliveryAddress())
                .orderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build();
        newDelivery = deliveryFeign.planDelivery(newDelivery);
        order.setDeliveryId(newDelivery.getDeliveryId());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnProduct(ReturnProductRequestDto request) {
        Order order = findOrderById(request.getOrderId());
        warehouseFeign.acceptReturn(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto payForOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        PaymentResponseDto payment = paymentFeign.addPayment(orderMapper.toDto(order));
        order.setPaymentId(payment.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new UnauthorizedException("Username must not be blank");
        }
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order `%s` not found".formatted(orderId)));
    }
}
