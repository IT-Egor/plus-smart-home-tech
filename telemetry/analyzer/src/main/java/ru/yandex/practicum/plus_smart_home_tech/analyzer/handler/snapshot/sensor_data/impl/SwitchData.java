package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

@Component
public class SwitchData implements SensorData {
    @Override
    public ConditionType getType() {
        return ConditionType.SWITCH;
    }

    @Override
    public Integer getValue(Object data) {
        SwitchSensorAvro switchSensor = (SwitchSensorAvro) data;
        return switchSensor.getState() ? 1 : 0;
    }
}
