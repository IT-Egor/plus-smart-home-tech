package ru.yandex.practicum.plus_smart_home_tech.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.payment.model.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
