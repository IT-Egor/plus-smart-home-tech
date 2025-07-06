package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    Class<?> getPayloadClass();

    void handle(HubEventAvro event);
}
