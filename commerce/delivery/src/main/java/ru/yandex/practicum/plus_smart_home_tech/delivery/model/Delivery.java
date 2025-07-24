package ru.yandex.practicum.plus_smart_home_tech.delivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @OneToOne
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @OneToOne
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;
}
