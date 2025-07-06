package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

@Component
public class TemperatureData implements SensorData {
    @Override
    public ConditionType getType() {
        return ConditionType.TEMPERATURE;
    }

    @Override
    public Integer getValue(Object data) {
        return switch (data) {
            case ClimateSensorAvro climateSensor -> climateSensor.getTemperatureC();
            case TemperatureSensorAvro temperatureSensor -> temperatureSensor.getTemperatureC();
            default -> throw new IllegalArgumentException("Unsupported data type");
        };
    }
}
