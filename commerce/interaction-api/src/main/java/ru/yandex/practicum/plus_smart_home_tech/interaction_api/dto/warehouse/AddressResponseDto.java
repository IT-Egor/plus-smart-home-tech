package ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
