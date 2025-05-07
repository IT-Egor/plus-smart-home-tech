package ru.practicum.plus_smart_home_tech.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.kafka.KafkaClient;
import ru.practicum.plus_smart_home_tech.mapper.hub.HubEventAvroMapperFactory;
import ru.practicum.plus_smart_home_tech.service.HubEventCollectorService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Service
@RequiredArgsConstructor
public class HubEventCollectorServiceImpl implements HubEventCollectorService {
    private final KafkaClient kafkaClient;
    private final HubEventAvroMapperFactory hubEventAvroMapperFactory;

    @Value(value = "${kafka.hub-event-topic:telemetry.hubs.v1}")
    private String topic;

    @Override
    public void collect(HubEvent event) {
        HubEventAvro hubEventAvro = hubEventAvroMapperFactory.getMapper(event.getType()).toAvro(event);
        kafkaClient.getProducer().send(new ProducerRecord<>(topic,hubEventAvro));
    }
}
