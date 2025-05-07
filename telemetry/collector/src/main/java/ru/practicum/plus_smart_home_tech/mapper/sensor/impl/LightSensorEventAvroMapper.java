package ru.practicum.plus_smart_home_tech.mapper.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.LightSensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class LightSensorEventAvroMapper implements SensorEventAvroMapper {
    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEvent event) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) event;

        Object payload = LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
