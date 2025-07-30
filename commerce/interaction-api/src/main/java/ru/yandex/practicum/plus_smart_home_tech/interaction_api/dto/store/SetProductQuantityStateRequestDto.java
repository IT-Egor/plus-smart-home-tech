package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.store;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.store.QuantityState;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequestDto {
    @NotNull(message = "Product id is required")
    private UUID productId;

    @NotNull(message = "Quantity state is required")
    private QuantityState quantityState;
}
