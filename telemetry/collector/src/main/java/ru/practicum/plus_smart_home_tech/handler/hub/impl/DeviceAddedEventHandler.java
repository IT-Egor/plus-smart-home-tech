package ru.practicum.plus_smart_home_tech.handler.hub.impl;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.plus_smart_home_tech.handler.hub.HubEventHandler;
import ru.practicum.plus_smart_home_tech.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@Component
@AllArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        HubEventAvro eventAvro = protoToAvro(eventProto);
        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                kafkaClient.getHubTopic(),
                null,
                eventAvro.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro);
        kafkaClient.getProducer().send(producerRecord);
        log.info("Scenario removed: {}", eventAvro);
    }

    @Override
    public HubEventAvro protoToAvro(HubEventProto eventProto) {
        DeviceAddedEventProto deviceAddedEventProto = eventProto.getDeviceAdded();

        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEventProto.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEventProto.getType().name()))
                .build();

        Timestamp timestamp = eventProto.getTimestamp();

        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()))
                .setPayload(deviceAddedEventAvro)
                .build();
    }
}
