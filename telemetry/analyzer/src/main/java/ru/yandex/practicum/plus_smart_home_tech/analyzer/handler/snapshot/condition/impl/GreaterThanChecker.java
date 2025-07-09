package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.ConditionChecker;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionOperation;

@Component
public class GreaterThanChecker implements ConditionChecker {
    @Override
    public ConditionOperation getType() {
        return ConditionOperation.GREATER_THAN;
    }

    @Override
    public Boolean check(Integer sensorValue, Integer conditionValue) {
        return sensorValue > conditionValue;
    }
}
