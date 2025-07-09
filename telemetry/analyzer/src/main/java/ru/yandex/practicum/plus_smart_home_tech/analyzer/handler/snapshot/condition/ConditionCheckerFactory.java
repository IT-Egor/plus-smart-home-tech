package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConditionCheckerFactory {
    Map<ConditionOperation, ConditionChecker> checkers = new HashMap<>();

    public ConditionCheckerFactory(List<ConditionChecker> checkers) {
        for (ConditionChecker checker : checkers) {
            this.checkers.put(checker.getType(), checker);
        }
    }

    public ConditionChecker getChecker(ConditionOperation type) {
        if (checkers.containsKey(type)) {
            return checkers.get(type);
        } else {
            throw new IllegalArgumentException("Condition checker for type " + type + " not found");
        }
    }
}
