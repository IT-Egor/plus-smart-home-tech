package ru.practicum.plus_smart_home_tech.mapper.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.practicum.plus_smart_home_tech.dto.hub.device.removed.DeviceRemovedEvent;
import ru.practicum.plus_smart_home_tech.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceRemovedEventAvroMapper implements HubEventAvroMapper {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public HubEventAvro toAvro(HubEvent event) {
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;

        Object payload = DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
