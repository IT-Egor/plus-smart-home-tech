package ru.yandex.practicum.plus_smart_home_tech.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.warehouse.model.WarehouseProduct;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, UUID> {
}
