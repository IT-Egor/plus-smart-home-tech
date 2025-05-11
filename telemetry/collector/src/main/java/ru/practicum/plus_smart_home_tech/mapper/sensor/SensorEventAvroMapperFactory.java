package ru.practicum.plus_smart_home_tech.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEventType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SensorEventAvroMapperFactory {
    private final Map<SensorEventType, SensorEventAvroMapper> mappers;

    public SensorEventAvroMapperFactory(List<SensorEventAvroMapper> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(SensorEventAvroMapper::getType, mapper -> mapper));
    }

    public SensorEventAvroMapper getMapper(SensorEventType eventType) {
        if (!mappers.containsKey(eventType)) {
            throw new IllegalArgumentException("Unknown sensor event type: " + eventType);
        }
        return mappers.get(eventType);
    }
}
