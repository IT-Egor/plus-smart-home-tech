package ru.yandex.practicum.aggregator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SensorEventUpdater {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(event.getHubId(), k ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setSensorsState(new HashMap<>())
                        .setTimestamp(Instant.now())
                        .build());

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        if (isUpdated(sensorsState, event)) {
            return Optional.empty();
        }

        SensorStateAvro sensorStateAvro = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        sensorsState.put(event.getId(), sensorStateAvro);
        snapshot.setSensorsState(sensorsState);
        snapshot.setTimestamp(event.getTimestamp());

        return Optional.of(snapshot);
    }

    private boolean isUpdated(Map<String, SensorStateAvro> sensorsState, SensorEventAvro event) {
        if (sensorsState != null && sensorsState.containsKey(event.getId())) {
            SensorStateAvro oldState = sensorsState.get(event.getId());
            return (oldState.getTimestamp().isAfter(event.getTimestamp()))
                    || (oldState.getData().equals(event.getPayload()));
        } else {
            return false;
        }
    }
}