package ru.yandex.practicum.plus_smart_home_tech.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HubEventHandlerFactory {
    private final Map<HubEventProto.PayloadCase, HubEventHandler> mappers;

    public HubEventHandlerFactory(List<HubEventHandler> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, mapper -> mapper));
    }

    public HubEventHandler getHandler(HubEventProto.PayloadCase payloadCase) {
        if (!mappers.containsKey(payloadCase)) {
            throw new IllegalArgumentException("Unknown hub event type: " + payloadCase);
        }
        return mappers.get(payloadCase);
    }
}
