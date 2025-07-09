package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ActionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ConditionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ScenarioRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.grpc.HubRouterGrpcClient;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.ConditionChecker;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.ConditionCheckerFactory;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorDataFactory;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.mapper.EntityMapper;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Condition;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Scenario;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotHandler {
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final HubRouterGrpcClient hubRouterGrpcClient;
    private final SensorDataFactory sensorDataFactory;
    private final ConditionCheckerFactory conditionCheckerFactory;

    public void handle(SensorsSnapshotAvro snapshot) {
        log.debug("Processing snapshot '{}'", snapshot);
        final String hubId = snapshot.getHubId();
        final Map<String, SensorStateAvro> sensorStates = snapshot.getSensorsState();

        Map<Scenario, List<Condition>> scenariosWithConditions = scenarioRepository
                .findByHubId(hubId).stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        scenario -> conditionRepository.findAllByScenarioId(scenario.getId()).stream().toList()
                ));

        scenariosWithConditions.entrySet().stream()
                .filter(entry -> checkScenario(entry.getValue(), sensorStates))
                .forEach(entry -> {
                    Scenario scenario = entry.getKey();
                    log.info("Scenario triggered: {}", scenario.getName());
                    sendAction(scenario);
                });
    }

    private Boolean checkScenario(List<Condition> conditions, Map<String, SensorStateAvro> sensorStates) {
        return conditions.stream()
                .allMatch(condition -> checkCondition(condition, sensorStates));
    }

    private Boolean checkCondition(Condition condition, Map<String, SensorStateAvro> sensorState) {
        SensorStateAvro sensorStateAvro = sensorState.get(condition.getSensor().getId());

        if (sensorStateAvro == null) {
            return false;
        }

        SensorData sensorData = sensorDataFactory.getSensorData(condition.getType());
        ConditionChecker conditionChecker = conditionCheckerFactory.getChecker(condition.getOperation());

        return conditionChecker.check(sensorData.getValue(sensorStateAvro.getData()), condition.getValue());
    }

    private void sendAction(Scenario scenario) {
        actionRepository.findAllByScenarioId(scenario.getId()).stream()
                .map(EntityMapper::actionToGrpcRequest)
                .forEach(hubRouterGrpcClient::send);
    }
}
