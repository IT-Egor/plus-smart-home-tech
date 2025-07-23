package ru.yandex.practicum.plus_smart_home_tech.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order_booking")
public class OrderBooking {
    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ElementCollection
    @Column(name = "quantity")
    @MapKeyColumn(name = "product_id")
    @CollectionTable(name = "booking_products", joinColumns = @JoinColumn(name = "order_booking_id"))
    private Map<UUID, Long> products;
}
