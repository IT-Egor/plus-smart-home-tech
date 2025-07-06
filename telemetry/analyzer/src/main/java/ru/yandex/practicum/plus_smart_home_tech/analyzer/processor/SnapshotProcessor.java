package ru.yandex.practicum.plus_smart_home_tech.analyzer.processor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.snapshot.SnapshotHandler;
import ru.yandex.practicum.plus_smart_home_tech.serialization.kafka.deserialization.SensorSnapshotDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class SnapshotProcessor {
    private final List<String> topics;
    private final String groupId;
    private final String bootstrapServer;

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler handler;

    public SnapshotProcessor(@Value("${app.kafka.snapshot.bootstrap-server}") String bootstrapServer,
                             @Value("${app.kafka.snapshot.consumer-topics}") List<String> topics,
                             @Value("${app.kafka.snapshot.group-id}") String groupId,
                             SnapshotHandler handler) {
        this.bootstrapServer = bootstrapServer;
        this.topics = topics;
        this.groupId = groupId;
        this.handler = handler;
        consumer = new KafkaConsumer<>(getConsumerProperties());
    }

    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    SensorsSnapshotAvro sensorsSnapshotAvro = record.value();
                    log.info("Received snapshot from hub id '{}'", sensorsSnapshotAvro.getHubId());
                    handler.handle(sensorsSnapshotAvro);
                }
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Error:", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
                log.info("Consumer close");
            }
        }
    }

    @PreDestroy
    public void stop() {
        consumer.wakeup();
    }

    private Properties getConsumerProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorSnapshotDeserializer.class);
        return properties;
    }
}
