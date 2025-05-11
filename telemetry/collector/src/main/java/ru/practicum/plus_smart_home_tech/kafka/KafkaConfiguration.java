package ru.practicum.plus_smart_home_tech.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.plus_smart_home_tech.kafka.serialization.GeneralAvroSerializer;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.server:localhost:9092}")
    private String kafkaServer;

    @Value(value = "${kafka.hub-event-topic:telemetry.hubs.v1}")
    private String hubTopic;

    @Value(value = "${kafka.sensor-event-topic:telemetry.sensors.v1}")
    private String sensorTopic;

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {

            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

                producer = new KafkaProducer<>(config);
            }

            @Override
            @PreDestroy
            public void stop() {
                if (producer != null) {
                    producer.flush();
                    producer.close();
                }
            }

            @Override
            public String getHubTopic() {
                return hubTopic;
            }

            @Override
            public String getSensorTopic() {
                return sensorTopic;
            }
        };
    }
}
