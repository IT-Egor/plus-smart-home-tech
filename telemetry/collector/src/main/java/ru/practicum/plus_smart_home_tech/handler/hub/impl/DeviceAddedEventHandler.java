package ru.practicum.plus_smart_home_tech.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.device.added.DeviceAddedEvent;
import ru.practicum.plus_smart_home_tech.handler.hub.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceAddedEventHandler implements HubEventHandler {
    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
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
