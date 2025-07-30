package ru.yandex.practicum.plus_smart_home_tech.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.OrderBooking;

import java.util.UUID;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {
}
