package ru.practicum.plus_smart_home_tech.mapper.hub;

import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventAvroMapper {
    HubEventType getType();

    HubEventAvro toAvro(HubEvent event);
}
