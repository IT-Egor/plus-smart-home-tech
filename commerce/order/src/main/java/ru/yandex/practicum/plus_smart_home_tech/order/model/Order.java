package ru.yandex.practicum.plus_smart_home_tech.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", unique = true)
    private UUID orderId;

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @ElementCollection
    @Column(name = "quantity")
    @MapKeyColumn(name = "product_id")
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    private Map<UUID, Long> products = new HashMap<>();

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "product_price")
    private BigDecimal productPrice;
}
