package ru.practicum.plus_smart_home_tech.mapper.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.removed.ScenarioRemovedEvent;
import ru.practicum.plus_smart_home_tech.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventAvroMapper implements HubEventAvroMapper {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
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
