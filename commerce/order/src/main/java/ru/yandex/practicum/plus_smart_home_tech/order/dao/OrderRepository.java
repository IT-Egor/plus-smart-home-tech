package ru.yandex.practicum.plus_smart_home_tech.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByShoppingCartId(UUID shoppingCartId);
}
