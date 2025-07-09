package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ActionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ConditionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ScenarioRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.SensorRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.exception.BadRequestException;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.mapper.EntityMapper;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Scenario;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Sensor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public Class<?> getPayloadClass() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro addedEvent = (ScenarioAddedEventAvro) event.getPayload();

        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(event.getHubId(), addedEvent.getName());
        try {
            if (scenarioOpt.isPresent()) {
                updateScenario(scenarioOpt.get(), addedEvent, event.getHubId());
            } else {
                addScenario(event.getHubId(), addedEvent);
            }
        } catch (Exception e) {
            log.error("Scenario '{}' in hub id '{}' is not valid or not found", addedEvent.getName(), event.getHubId(), e);
        }
    }

    private void addScenario(String hubId, ScenarioAddedEventAvro event) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(event.getName());
        scenarioRepository.save(scenario);
        addConditions(scenario, event);
        addActions(scenario, event);
        log.info("Scenario '{}' added to hub id '{}'", scenario.getName(), hubId);
    }

    private void updateScenario(Scenario scenario, ScenarioAddedEventAvro event, String hubId) {
        scenario.setHubId(hubId);
        scenario.setName(event.getName());
        actionRepository.deleteByScenarioId(scenario.getId());
        conditionRepository.deleteByScenarioId(scenario.getId());
        addActions(scenario, event);
        addConditions(scenario, event);
        log.info("Scenario '{}' updated in hub id '{}'", scenario.getName(), scenario.getHubId());
    }

    private void addConditions(Scenario scenario, ScenarioAddedEventAvro event) {
        addScenarioElements(
                scenario,
                event,
                ScenarioAddedEventAvro::getConditions,
                ScenarioConditionAvro::getSensorId,
                (conditionAvro, sensor) -> EntityMapper.avroToCondition(scenario, conditionAvro, sensor),
                conditionRepository
        );
    }

    private void addActions(Scenario scenario, ScenarioAddedEventAvro event) {
        addScenarioElements(
                scenario,
                event,
                ScenarioAddedEventAvro::getActions,
                DeviceActionAvro::getSensorId,
                (actionAvro, sensor) -> EntityMapper.avroToAction(scenario, actionAvro, sensor),
                actionRepository
        );
    }

    private <T, R> void addScenarioElements(
            Scenario scenario,
            ScenarioAddedEventAvro event,
            Function<ScenarioAddedEventAvro, List<T>> itemsExtractor,
            Function<T, String> sensorIdExtractor,
            BiFunction<T, Sensor, R> entityMapper,
            JpaRepository<R, ?> repository
    ) {
        List<T> items = itemsExtractor.apply(event);
        if (items.isEmpty()) {
            return;
        }

        List<String> sensorIds = items.stream()
                .map(sensorIdExtractor)
                .toList();

        List<Sensor> sensors = sensorRepository.findAllById(sensorIds);
        checkSensorsHub(scenario, sensors);

        Map<String, Sensor> sensorMap = sensors.stream()
                .collect(Collectors.toMap(Sensor::getId, sensor -> sensor));

        List<R> entities = items.stream()
                .map(item -> {
                    String sensorId = sensorIdExtractor.apply(item);
                    Sensor sensor = sensorMap.get(sensorId);
                    return entityMapper.apply(item, sensor);
                })
                .toList();

        repository.saveAll(entities);
    }

    private void checkSensorsHub(Scenario scenario, List<Sensor> sensors) {
        boolean isHubSame = sensors.stream()
                .map(Sensor::getHubId)
                .anyMatch(hubId -> !hubId.equals(scenario.getHubId()));
        if (isHubSame) {
            throw new BadRequestException("Hub id of scenario and sensors must be the same");
        }
    }
}
