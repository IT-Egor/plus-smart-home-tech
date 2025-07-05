package ru.yandex.practicum.plus_smart_home_tech.collector.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    @NotNull(message = "linkQuality is required")
    private int linkQuality;

    @NotNull(message = "motion is required")
    private boolean motion;

    @NotNull(message = "voltage is required")
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
