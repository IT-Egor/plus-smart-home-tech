package ru.practicum.plus_smart_home_tech.mapper.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.MotionSensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class MotionSensorEventAvroMapper implements SensorEventAvroMapper {
    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEvent event) {
        MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;

        Object payload = MotionSensorAvro.newBuilder()
                .setMotion(motionSensorEvent.isMotion())
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
