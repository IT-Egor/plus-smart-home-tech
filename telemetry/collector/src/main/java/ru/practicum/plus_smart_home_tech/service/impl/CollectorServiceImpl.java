package ru.practicum.plus_smart_home_tech.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.plus_smart_home_tech.dto.sensor.*;
import ru.practicum.plus_smart_home_tech.kafka.KafkaClient;
import ru.practicum.plus_smart_home_tech.service.CollectorService;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final KafkaClient kafkaClient;
    private final String topic = "telemetry.sensors.v1";

    @Override
    public void collect(SensorEvent event) {
        SensorEventAvro sensorEventAvro = mapToAvro(event);
        kafkaClient.getProducer().send(new ProducerRecord<>(topic, sensorEventAvro));
    }

    private SensorEventAvro mapToAvro(SensorEvent event) {
        Object payload;
        switch (event) {
            case ClimateSensorEvent climateSensorEvent ->
                    payload = ClimateSensorAvro.newBuilder()
                            .setCo2Level(climateSensorEvent.getCo2Level())
                            .setHumidity(climateSensorEvent.getHumidity())
                            .setTemperatureC(climateSensorEvent.getTemperatureC())
                            .build();

            case LightSensorEvent lightSensorEvent ->
                    payload = LightSensorAvro.newBuilder()
                            .setLinkQuality(lightSensorEvent.getLinkQuality())
                            .setLuminosity(lightSensorEvent.getLuminosity())
                            .build();

            case MotionSensorEvent motionSensorEvent ->
                    payload = MotionSensorAvro.newBuilder()
                            .setMotion(motionSensorEvent.isMotion())
                            .setLinkQuality(motionSensorEvent.getLinkQuality())
                            .setVoltage(motionSensorEvent.getVoltage())
                            .build();

            case SwitchSensorEvent switchSensorEvent ->
                    payload = SwitchSensorAvro.newBuilder()
                            .setState(switchSensorEvent.isState())
                            .build();

            case TemperatureSensorEvent temperatureSensorEvent ->
                    payload = TemperatureSensorAvro.newBuilder()
                            .setTemperatureC(Objects.requireNonNull(temperatureSensorEvent).getTemperatureC())
                            .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                            .build();

            default -> throw new IllegalArgumentException("Unexpected event: " + event);
        }

        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
