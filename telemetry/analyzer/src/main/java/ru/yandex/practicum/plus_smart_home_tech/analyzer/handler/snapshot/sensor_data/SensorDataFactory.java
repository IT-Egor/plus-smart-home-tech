package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.sensor_data;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.ConditionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SensorDataFactory {
    Map<ConditionType, SensorData> sensors = new HashMap<>();

    public SensorDataFactory(List<SensorData> sensors) {
        for (SensorData sensor : sensors) {
            this.sensors.put(sensor.getType(), sensor);
        }
    }

    public SensorData getSensorData(ConditionType type) {
        if (!sensors.containsKey(type)) {
            throw new IllegalArgumentException("Unknown sensor data type: " + type);
        }
        return sensors.get(type);
    }
}
