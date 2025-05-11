package ru.practicum.plus_smart_home_tech.handler.hub;

import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    HubEventProto.PayloadCase getType();

    HubEventAvro toAvro(HubEvent event);
}
