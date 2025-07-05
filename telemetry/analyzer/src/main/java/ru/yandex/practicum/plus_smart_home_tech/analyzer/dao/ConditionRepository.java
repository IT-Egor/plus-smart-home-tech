package ru.yandex.practicum.plus_smart_home_tech.analyzer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
