package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition;

import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionOperation;

public interface ConditionChecker {
    ConditionOperation getType();

    Boolean check(Integer sensorValue, Integer conditionValue);
}
