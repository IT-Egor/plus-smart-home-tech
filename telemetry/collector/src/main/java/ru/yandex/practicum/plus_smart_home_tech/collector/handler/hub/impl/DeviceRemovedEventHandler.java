package ru.yandex.practicum.plus_smart_home_tech.collector.handler.hub.impl;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.plus_smart_home_tech.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.plus_smart_home_tech.collector.kafka.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@Component
@AllArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
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
        log.info("Device removed: {}", eventAvro);
    }

    @Override
    public HubEventAvro protoToAvro(HubEventProto eventProto) {
        DeviceRemovedEventProto deviceRemovedEventProto = eventProto.getDeviceRemoved();

        DeviceRemovedEventAvro deviceRemovedEventAvro = DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEventProto.getId())
                .build();

        Timestamp timestamp = eventProto.getTimestamp();

        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()))
                .setPayload(deviceRemovedEventAvro)
                .build();
    }
}
