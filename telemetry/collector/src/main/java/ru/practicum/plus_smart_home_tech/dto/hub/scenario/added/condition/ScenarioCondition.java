package ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.condition;

import lombok.Data;

@Data
public class ScenarioCondition {
    private String sensorId;
    private ConditionType type;
    private Operation operation;
    private int value;
}
