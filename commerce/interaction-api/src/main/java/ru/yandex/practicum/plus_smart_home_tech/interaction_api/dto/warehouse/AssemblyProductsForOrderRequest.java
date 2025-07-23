package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyProductsForOrderRequest {
    @NotNull(message = "Order id is required")
    private UUID orderId;

    @NotNull(message = "Products is required")
    @NotEmpty(message = "Products is required")
    private Map<UUID, Long> products;
}
