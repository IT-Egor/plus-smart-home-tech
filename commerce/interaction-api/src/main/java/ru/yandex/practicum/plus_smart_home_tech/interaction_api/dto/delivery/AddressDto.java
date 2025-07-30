package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    @NotNull(message = "Country is required")
    private String country;

    @NotNull(message = "City is required")
    private String city;

    @NotNull(message = "Street is required")
    private String street;

    @NotNull(message = "House is required")
    private String house;

    @NotNull(message = "Flat is required")
    private String flat;
}
