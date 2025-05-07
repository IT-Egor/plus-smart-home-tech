package ru.practicum.plus_smart_home_tech.mapper.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.ClimateSensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class ClimateSensorEventAvroMapper implements SensorEventAvroMapper {
    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEvent event) {
        ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;

        Object payload = ClimateSensorAvro.newBuilder()
                .setCo2Level(climateSensorEvent.getCo2Level())
                .setHumidity(climateSensorEvent.getHumidity())
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
