package ru.yandex.practicum.plus_smart_home_tech.collector.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull(message = "temperatureC is required")
    private int temperatureC;

    @NotNull(message = "temperatureF is required")
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
