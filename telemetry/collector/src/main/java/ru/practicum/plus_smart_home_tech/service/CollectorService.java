package ru.practicum.plus_smart_home_tech.service;

import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;

public interface CollectorService {
    void collect(SensorEvent sensorEvent);
}
