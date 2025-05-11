package ru.practicum.plus_smart_home_tech.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.plus_smart_home_tech.dto.sensor.*;
import ru.practicum.plus_smart_home_tech.kafka.KafkaClient;
import ru.practicum.plus_smart_home_tech.mapper.sensor.SensorEventAvroMapperFactory;
import ru.practicum.plus_smart_home_tech.service.SensorEventCollectorService;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventCollectorServiceImpl implements SensorEventCollectorService {
    private final KafkaClient kafkaClient;
    private final SensorEventAvroMapperFactory sensorEventAvroMapperFactory;

    @Value("${kafka.sensor-event-topic:telemetry.sensors.v1}")
    private String topic;

    @Override
    public void collect(SensorEvent event) {
        SensorEventAvro sensorEventAvro = sensorEventAvroMapperFactory.getMapper(event.getType()).toAvro(event);
        kafkaClient.getProducer().send(new ProducerRecord<>(
                topic,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                sensorEventAvro));
        log.info("Sensor event collected: {}", sensorEventAvro);
    }
}
