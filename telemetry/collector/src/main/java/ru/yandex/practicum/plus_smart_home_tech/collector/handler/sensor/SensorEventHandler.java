package ru.yandex.practicum.plus_smart_home_tech.collector.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorEventHandler {
    SensorEventProto.PayloadCase getType();

    void handle(SensorEventProto eventProto);

    SensorEventAvro protoToAvro(SensorEventProto eventProto);
}
