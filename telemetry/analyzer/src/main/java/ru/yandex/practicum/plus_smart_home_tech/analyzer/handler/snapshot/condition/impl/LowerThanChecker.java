package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.ConditionChecker;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionOperation;

@Component
public class LowerThanChecker implements ConditionChecker {
    @Override
    public ConditionOperation getType() {
        return ConditionOperation.LOWER_THAN;
    }

    @Override
    public Boolean check(Integer sensorValue, Integer conditionValue) {
        return sensorValue < conditionValue;
    }
}
