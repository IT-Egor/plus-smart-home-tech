package ru.practicum.plus_smart_home_tech.mapper.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.practicum.plus_smart_home_tech.dto.sensor.TemperatureSensorEvent;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventAvroMapper implements SensorEventAvroMapper {
    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEvent event) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;

        Object payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
