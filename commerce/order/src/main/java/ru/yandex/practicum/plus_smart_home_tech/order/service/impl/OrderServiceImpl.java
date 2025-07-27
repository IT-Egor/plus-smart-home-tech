package ru.yandex.practicum.plus_smart_home_tech.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.UnauthorizedException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.ShoppingCartFeign;
import ru.yandex.practicum.plus_smart_home_tech.order.dao.OrderRepository;
import ru.yandex.practicum.plus_smart_home_tech.order.mapper.OrderMapper;
import ru.yandex.practicum.plus_smart_home_tech.order.service.OrderService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartFeign shoppingCartFeign;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        checkUserAuthorization(username);
        ShoppingCartDto shoppingCartDto = shoppingCartFeign.getShoppingCart(username);
        return orderRepository.findByShoppingCartId(shoppingCartDto.getShoppingCartId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new UnauthorizedException("Username must not be blank");
        }
    }
}
