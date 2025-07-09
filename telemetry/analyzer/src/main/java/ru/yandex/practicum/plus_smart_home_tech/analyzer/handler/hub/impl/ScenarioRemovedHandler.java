package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ActionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ConditionRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.ScenarioRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Scenario;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedHandler implements HubEventHandler {
    final ActionRepository actionRepository;
    final ConditionRepository conditionRepository;
    final ScenarioRepository scenarioRepository;

    @Override
    public Class<?> getPayloadClass() {
        return ScenarioRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro removedEvent = (ScenarioRemovedEventAvro) event.getPayload();

        String name = removedEvent.getName();
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(event.getHubId(), name);
        if (scenarioOpt.isPresent()) {
            Scenario scenario = scenarioOpt.get();
            conditionRepository.deleteByScenarioId(scenario.getId());
            actionRepository.deleteByScenarioId(scenario.getId());
            scenarioRepository.deleteById(scenario.getId());
        }
        log.info("Scenario '{}' deleted from hub id '{}'", name, event.getHubId());
    }
}
