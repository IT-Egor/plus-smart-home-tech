package ru.yandex.practicum.plus_smart_home_tech.collector.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    @NotNull(message = "temperatureC is required")
    private int temperatureC;

    @NotNull(message = "humidity is required")
    private int humidity;

    @NotNull(message = "co2Level is required")
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
