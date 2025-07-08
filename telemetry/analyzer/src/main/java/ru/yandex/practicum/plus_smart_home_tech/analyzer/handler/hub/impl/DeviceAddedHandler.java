package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.SensorRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.mapper.EntityMapper;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.model.Sensor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {
    final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadClass() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) event.getPayload();

        Sensor sensor = EntityMapper.deviceAvroToSensor(deviceAddedEvent, event.getHubId());

        if (!sensorRepository.existsById(sensor.getId())) {
            sensorRepository.save(sensor);
            log.info("Device added '{}'", sensor);
        }
    }
}
