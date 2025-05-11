package ru.practicum.plus_smart_home_tech.mapper.sensor;

import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorEventAvroMapper {
    SensorEventType getType();

    SensorEventAvro toAvro(SensorEvent event);
}
