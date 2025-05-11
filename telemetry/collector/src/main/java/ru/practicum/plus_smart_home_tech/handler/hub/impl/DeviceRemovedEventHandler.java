package ru.practicum.plus_smart_home_tech.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.device.removed.DeviceRemovedEvent;
import ru.practicum.plus_smart_home_tech.handler.hub.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceRemovedEventHandler implements HubEventHandler {
    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
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
