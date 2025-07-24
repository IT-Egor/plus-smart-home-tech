package ru.yandex.practicum.plus_smart_home_tech.delivery.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.delivery.rates")
public class DeliveryCostRates {
    private final double base;
    private final int address1;
    private final int address2;
    private final double fragile;
    private final double weight;
    private final double volume;
    private final double street;
}
