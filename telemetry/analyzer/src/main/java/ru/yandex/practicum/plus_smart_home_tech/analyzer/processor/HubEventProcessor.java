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
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub.HubEventHandlerFactory;
import ru.yandex.practicum.plus_smart_home_tech.serialization.kafka.deserialization.HubEventDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final List<String> topics;
    private final String groupId;
    private final String bootstrapServer;

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final HubEventHandlerFactory handlerFactory;
    private final KafkaConsumer<String, HubEventAvro> consumer;

    public HubEventProcessor(@Value("${app.kafka.hub.bootstrap-server}") String bootstrapServer,
                             @Value("${app.kafka.hub.consumer-topics}") List<String> topics,
                             @Value("${app.kafka.hub.group-id}") String groupId,
                             HubEventHandlerFactory handlerFactory) {
        this.bootstrapServer = bootstrapServer;
        this.topics = topics;
        this.groupId = groupId;
        this.handlerFactory = handlerFactory;
        consumer = new KafkaConsumer<>(getConsumerProperties());
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEventAvro = record.value();
                    log.info("Received hub event from hub id '{}'", hubEventAvro.getHubId());
                    HubEventHandler handler = handlerFactory.getHandler(hubEventAvro.getPayload().getClass());
                    handler.handle(hubEventAvro);
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
                log.info("Consumer closed");
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
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
        return properties;
    }
}
