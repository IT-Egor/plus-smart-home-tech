package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data.SensorData;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

@Component
public class HummidityData implements SensorData {
    @Override
    public ConditionType getType() {
        return ConditionType.HUMIDITY;
    }

    @Override
    public Integer getValue(Object data) {
        ClimateSensorAvro humiditySensor = (ClimateSensorAvro) data;
        return humiditySensor.getHumidity();
    }
}
