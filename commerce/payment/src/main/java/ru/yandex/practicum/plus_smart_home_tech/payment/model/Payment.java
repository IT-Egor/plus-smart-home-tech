package ru.yandex.practicum.plus_smart_home_tech.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "delivery_total")
    private BigDecimal deliveryTotal;

    @Column(name = "vat_total")
    private BigDecimal vatTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
}
