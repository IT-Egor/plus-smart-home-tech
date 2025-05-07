package ru.practicum.plus_smart_home_tech.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.device.added.DeviceAddedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.device.removed.DeviceRemovedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.ScenarioAddedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.condition.ScenarioCondition;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.device_action.DeviceAction;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.removed.ScenarioRemovedEvent;
import ru.practicum.plus_smart_home_tech.kafka.KafkaClient;
import ru.practicum.plus_smart_home_tech.service.HubEventCollectorService;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubEventCollectorServiceImpl implements HubEventCollectorService {
    private final KafkaClient kafkaClient;
    private final String topic = "telemetry.hubs.v1";

    @Override
    public void collect(HubEvent event) {
        HubEventAvro hubEventAvro = mapToAvro(event);
        kafkaClient.getProducer().send(new ProducerRecord<>(topic,hubEventAvro));
    }

    private HubEventAvro mapToAvro(HubEvent event) {
        Object payload;
        switch (event) {
            case DeviceAddedEvent deviceAddedEvent -> payload = DeviceAddedEventAvro.newBuilder()
                    .setId(deviceAddedEvent.getId())
                    .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                    .build();

            case DeviceRemovedEvent deviceRemovedEvent -> payload = DeviceRemovedEventAvro.newBuilder()
                    .setId(deviceRemovedEvent.getId())
                    .build();

            case ScenarioAddedEvent scenarioAddedEvent -> {
                List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEvent.getActions().stream()
                        .map(this::deviceActionToAvro)
                        .toList();
                List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEvent.getConditions().stream()
                        .map(this::scenarioConditionAvro)
                        .toList();
                payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(scenarioAddedEvent.getName())
                        .setActions(deviceActionAvroList)
                        .setConditions(scenarioConditionAvroList)
                        .build();
            }

            case ScenarioRemovedEvent scenarioRemovedEvent ->
                    payload = ScenarioRemovedEventAvro.newBuilder()
                            .setName(scenarioRemovedEvent.getName())
                            .build();

            default -> throw new IllegalArgumentException("Unsupported hub event type");
        }

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
