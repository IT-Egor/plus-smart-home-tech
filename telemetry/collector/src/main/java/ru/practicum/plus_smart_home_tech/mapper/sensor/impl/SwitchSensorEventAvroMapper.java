package ru.practicum.plus_smart_home_tech.mapper.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.practicum.plus_smart_home_tech.dto.sensor.SwitchSensorEvent;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventAvroMapper implements SensorEventAvroMapper {
    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEvent event) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;

        Object payload = SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.isState())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
