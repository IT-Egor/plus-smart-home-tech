package ru.yandex.practicum.plus_smart_home_tech.collector.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.plus_smart_home_tech.serialization.kafka.serialization.GeneralAvroSerializer;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {
    private final String kafkaServer;
    private final String hubTopic;
    private final String sensorTopic;

    public KafkaConfiguration(@Value("${app.kafka.server}") String kafkaServer,
                              @Value(value = "${app.kafka.hub-event-topic}") String hubTopic,
                              @Value(value = "${app.kafka.sensor-event-topic}") String sensorTopic) {
        this.kafkaServer = kafkaServer;
        this.hubTopic = hubTopic;
        this.sensorTopic = sensorTopic;
    }

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
