package ru.practicum.plus_smart_home_tech.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient {
    Producer<String, SpecificRecordBase> getProducer();

    void stop();
}