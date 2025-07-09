package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

@Component
public class LuminosityData implements SensorData {
    @Override
    public ConditionType getType() {
        return ConditionType.LUMINOSITY;
    }

    @Override
    public Integer getValue(Object data) {
        LightSensorAvro lightSensor = (LightSensorAvro) data;
        return lightSensor.getLuminosity();
    }
}
