package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.dao.SensorRepository;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {
    final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadClass() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedAvro = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.deleteById(deviceRemovedAvro.getId());
        log.info("Device removed '{}'", deviceRemovedAvro);
    }
}