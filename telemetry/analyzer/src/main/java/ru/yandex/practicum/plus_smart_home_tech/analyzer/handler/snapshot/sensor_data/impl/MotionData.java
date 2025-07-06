package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

@Component
public class MotionData implements SensorData {
    @Override
    public ConditionType getType() {
        return ConditionType.MOTION;
    }

    @Override
    public Integer getValue(Object data) {
        MotionSensorAvro motionSensor = (MotionSensorAvro) data;
        return motionSensor.getMotion() ? 1 : 0;
    }
}
