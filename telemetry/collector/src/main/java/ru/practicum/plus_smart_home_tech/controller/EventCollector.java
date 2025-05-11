package ru.practicum.plus_smart_home_tech.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.plus_smart_home_tech.handler.hub.HubEventHandlerFactory;
import ru.practicum.plus_smart_home_tech.handler.sensor.SensorEventHandlerFactory;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Slf4j
@GrpcService
@AllArgsConstructor
public class EventCollector extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final SensorEventHandlerFactory sensorEventHandlerFactory;
    private final HubEventHandlerFactory hubEventHandlerFactory;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Sensor event collect request: {}", request);
            sensorEventHandlerFactory.getHandler(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Received request to collect sensor event: {}", request);
            sensorEventHandlerFactory.getHandler(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}

