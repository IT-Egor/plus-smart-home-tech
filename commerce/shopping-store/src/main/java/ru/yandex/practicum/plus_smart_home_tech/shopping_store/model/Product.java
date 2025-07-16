package ru.yandex.practicum.plus_smart_home_tech.shopping_store.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.ProductState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store.enums.QuantityState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "image_src", columnDefinition = "TEXT")
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", length = 50, nullable = false)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", length = 50, nullable = false)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", length = 50)
    private ProductCategory productCategory;

    @Column(name = "price")
    private Double price;
}
