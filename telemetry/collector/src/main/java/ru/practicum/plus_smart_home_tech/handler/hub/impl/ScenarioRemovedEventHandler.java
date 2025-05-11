package ru.practicum.plus_smart_home_tech.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.removed.ScenarioRemovedEvent;
import ru.practicum.plus_smart_home_tech.handler.hub.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {
    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public HubEventAvro toAvro(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;

        Object payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
