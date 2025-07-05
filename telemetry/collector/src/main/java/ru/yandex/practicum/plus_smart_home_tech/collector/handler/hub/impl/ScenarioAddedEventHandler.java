package ru.yandex.practicum.plus_smart_home_tech.collector.handler.hub.impl;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        HubEventAvro eventAvro = protoToAvro(eventProto);
        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                kafkaClient.getHubTopic(),
                null,
                eventAvro.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro);
        kafkaClient.getProducer().send(producerRecord);
        log.info("Scenario added: {}", eventAvro);
    }

    @Override
    public HubEventAvro protoToAvro(HubEventProto eventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = eventProto.getScenarioAdded();

        List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEventProto.getActionList().stream()
                .map(this::deviceActionToAvro)
                .toList();
        List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEventProto.getConditionList().stream()
                .map(this::scenarioConditionAvro)
                .toList();

        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setActions(deviceActionAvroList)
                .setConditions(scenarioConditionAvroList)
                .build();

        Timestamp timestamp = eventProto.getTimestamp();

        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()))
                .setPayload(scenarioAddedEventAvro)
                .build();
    }

    private DeviceActionAvro deviceActionToAvro(DeviceActionProto actionProto) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(actionProto.getType().name()))
                .setSensorId(actionProto.getSensorId())
                .setValue(actionProto.getValue())
                .build();
    }

    private ScenarioConditionAvro scenarioConditionAvro(ScenarioConditionProto conditionProto) {
        Object value = null;
        if (conditionProto.getValueCase() == ScenarioConditionProto.ValueCase.INT_VALUE) {
            value = conditionProto.getIntValue();
        } else if (conditionProto.getValueCase() == ScenarioConditionProto.ValueCase.BOOL_VALUE) {
            value = conditionProto.getBoolValue();
        }
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(conditionProto.getSensorId())
                .setType(ConditionTypeAvro.valueOf(conditionProto.getType().name()))
                .setValue(value)
                .setOperation(ConditionOperationAvro.valueOf(conditionProto.getOperation().name()))
                .build();
    }
}
