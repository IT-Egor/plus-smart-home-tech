package ru.yandex.practicum.plus_smart_home_tech.analyzer.mapper;

import com.google.protobuf.Timestamp;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.*;

import java.time.Instant;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EntityMapper {
    public static Sensor deviceAvroToSensor(DeviceAddedEventAvro deviceAddedEvent, String hubId) {
        Sensor sensor = new Sensor();
        sensor.setId(deviceAddedEvent.getId());
        sensor.setHubId(hubId);
        return sensor;
    }

    public static Condition avroToCondition(Scenario scenario, ScenarioConditionAvro conditionAvro, Sensor sensor) {
        Condition condition = new Condition();
        condition.setType(ConditionType.valueOf(conditionAvro.getType().name()));
        condition.setOperation(ConditionOperation.valueOf(conditionAvro.getOperation().name()));

        Object value = conditionAvro.getValue();
        if (value.getClass() == Integer.class) {
            condition.setValue((int) value);
        } else {
            condition.setValue(Boolean.TRUE.equals(value) ? 1 : 0);
        }

        condition.setScenario(scenario);
        condition.setSensor(sensor);
        return condition;
    }

    public static Action avroToAction(Scenario scenario, DeviceActionAvro deviceActionAvro, Sensor sensor) {
        Action action = new Action();
        action.setSensor(sensor);
        action.setScenario(scenario);
        action.setType(ActionType.valueOf(deviceActionAvro.getType().name()));
        action.setValue(deviceActionAvro.getValue());
        return action;
    }

    public static DeviceActionRequest actionToGrpcRequest(Action action) {
        return DeviceActionRequest.newBuilder()
                .setHubId(action.getScenario().getHubId())
                .setScenarioName(action.getScenario().getName())
                .setAction(actionToProto(action))
                .setTimestamp(createTimestamp())
                .build();
    }

    private static DeviceActionProto actionToProto(Action action) {
        return DeviceActionProto.newBuilder()
                .setSensorId(action.getSensor().getId())
                .setType(toActionTypeProto(action.getType()))
                .setValue(action.getValue())
                .build();
    }

    private static ActionTypeProto toActionTypeProto(ActionType actionType) {
        return switch (actionType) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }

    private static Timestamp createTimestamp() {
        Instant instant = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
