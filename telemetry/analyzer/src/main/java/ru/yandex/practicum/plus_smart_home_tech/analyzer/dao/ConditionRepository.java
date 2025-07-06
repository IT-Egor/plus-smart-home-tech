package ru.yandex.practicum.plus_smart_home_tech.analyzer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Condition;

import java.util.Collection;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    void deleteByScenarioId(Long scenarioId);

    Collection<Condition> findAllByScenarioId(Long scenarioId);
}
