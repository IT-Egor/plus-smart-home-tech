package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition.ConditionChecker;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionOperation;

import java.util.Objects;

@Component
public class EqualsChecker implements ConditionChecker {
    @Override
    public ConditionOperation getType() {
        return ConditionOperation.EQUALS;
    }

    @Override
    public Boolean check(Integer sensorValue, Integer conditionValue) {
        return Objects.equals(sensorValue, conditionValue);
    }
}
