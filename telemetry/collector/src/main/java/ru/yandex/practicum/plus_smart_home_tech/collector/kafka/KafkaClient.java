package ru.yandex.practicum.plus_smart_home_tech.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient {
    Producer<String, SpecificRecordBase> getProducer();

    String getHubTopic();

    String getSensorTopic();

    void stop();
}