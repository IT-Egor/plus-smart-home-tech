package ru.yandex.practicum.plus_smart_home_tech.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SensorEventHandlerFactory {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> mappers;

    public SensorEventHandlerFactory(List<SensorEventHandler> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getType, mapper -> mapper));
    }

    public SensorEventHandler getHandler(SensorEventProto.PayloadCase payloadCase) {
        if (!mappers.containsKey(payloadCase)) {
            throw new IllegalArgumentException("Unknown sensor event type: " + payloadCase);
        }
        return mappers.get(payloadCase);
    }
}
