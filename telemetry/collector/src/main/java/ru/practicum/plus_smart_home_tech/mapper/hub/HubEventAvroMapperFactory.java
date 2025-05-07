package ru.practicum.plus_smart_home_tech.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HubEventAvroMapperFactory {
    private final Map<HubEventType, HubEventAvroMapper> mappers;

    public HubEventAvroMapperFactory(List<HubEventAvroMapper> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(HubEventAvroMapper::getType, mapper -> mapper));
    }

    public HubEventAvroMapper getMapper(HubEventType eventType) {
        if (!mappers.containsKey(eventType)) {
            throw new IllegalArgumentException("Unknown hub event type: " + eventType);
        }
        return mappers.get(eventType);
    }
}
