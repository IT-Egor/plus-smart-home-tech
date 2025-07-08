package ru.yandex.practicum.plus_smart_home_tech.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
public class AggregationStarter implements CommandLineRunner {
    private final KafkaConfig kafkaConfig;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;

    private final SensorEventUpdater sensorEventUpdater;

    public AggregationStarter(SensorEventUpdater sensorEventUpdater,
                              KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
        this.sensorEventUpdater = sensorEventUpdater;
        consumer = new KafkaConsumer<>(getConsumerProperties());
        producer = new KafkaProducer<>(getProducerProperties());
    }

    @Override
    public void run(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        String snapshotTopic = kafkaConfig.getProducer().getTopic();

        try {
            consumer.subscribe(kafkaConfig.getConsumer().getTopics());

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

    private Properties getConsumerProperties() {
        Properties properties = new Properties();
        properties.putAll(kafkaConfig.getConsumer().getProperties());
        return properties;
    }

    private Properties getProducerProperties() {
        Properties properties = new Properties();
        properties.putAll(kafkaConfig.getProducer().getProperties());
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
