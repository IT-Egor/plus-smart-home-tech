package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data;

import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

public interface SensorData {
    ConditionType getType();

    Integer getValue(Object data);
}
