package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.store.ProductCategory;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.store.ProductState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.store.QuantityState;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product description is required")
    private String description;

    private String imageSrc;

    @NotNull(message = "Quantity state is required")
    private QuantityState quantityState;

    @NotNull(message = "Product state is required")
    private ProductState productState;

    private ProductCategory productCategory;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;
}
