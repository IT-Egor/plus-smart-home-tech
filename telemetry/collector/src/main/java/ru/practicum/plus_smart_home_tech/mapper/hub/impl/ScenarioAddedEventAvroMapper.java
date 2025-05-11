package ru.practicum.plus_smart_home_tech.mapper.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.ScenarioAddedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.condition.ScenarioCondition;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.device_action.DeviceAction;
import ru.practicum.plus_smart_home_tech.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedEventAvroMapper implements HubEventAvroMapper {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;

        List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEvent.getActions().stream()
                .map(this::deviceActionToAvro)
                .toList();
        List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEvent.getConditions().stream()
                .map(this::scenarioConditionAvro)
                .toList();

        Object payload = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(deviceActionAvroList)
                .setConditions(scenarioConditionAvroList)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private DeviceActionAvro deviceActionToAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .build();
    }

    private ScenarioConditionAvro scenarioConditionAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setValue(condition.getValue())
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .build();
    }
}
