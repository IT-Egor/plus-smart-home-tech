package ru.yandex.practicum.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.kafka.deserialization.SensorEventDeserializer;
import ru.practicum.plus_smart_home_tech.kafka.serialization.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class AggregationStarter implements CommandLineRunner {
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
    private final List<String> sensorTopics;
    private final String snapshotTopic;

    private final KafkaConsumer<String, SensorEventAvro> consumer = new KafkaConsumer<>(getConsumerProperties());
    private final KafkaProducer<String, SpecificRecordBase> producer = new KafkaProducer<>(getProducerProperties());

    private final SensorEventUpdater sensorEventUpdater;

    public AggregationStarter(SensorEventUpdater sensorEventUpdater,
                              @Value("${app.kafka.snapshot-topic}") String snapshotTopic,
                              @Value("${app.kafka.sensor-topics}") List<String> sensorTopics) {
        this.sensorEventUpdater = sensorEventUpdater;
        this.snapshotTopic = snapshotTopic;
        this.sensorTopics = sensorTopics;
    }

    @Override
    public void run(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(sensorTopics);

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> sensorsSnapshotAvroOpt = sensorEventUpdater.updateState(record.value());
                    if (sensorsSnapshotAvroOpt.isPresent()) {
                        SensorsSnapshotAvro sensorsSnapshotAvro = sensorsSnapshotAvroOpt.get();
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(snapshotTopic,
                                        null,
                                        sensorsSnapshotAvro.getTimestamp().getEpochSecond(),
                                        sensorsSnapshotAvro.getHubId(),
                                        sensorsSnapshotAvro);
                        producer.send(producerRecord);
                        log.info("Handled snapshot from hub ID = {}", sensorsSnapshotAvro.getHubId());
                    }
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignores) {

        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private static Properties getConsumerProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "SomeConsumer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "some.group.id");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        return properties;
    }

    private static Properties getProducerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        return properties;
    }

    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record,
                                      int count,
                                      KafkaConsumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}
