package ru.practicum.plus_smart_home_tech.mapper.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.practicum.plus_smart_home_tech.dto.hub.device.added.DeviceAddedEvent;
import ru.practicum.plus_smart_home_tech.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceAddedEventAvroMapper implements HubEventAvroMapper {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;

        Object payload = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
