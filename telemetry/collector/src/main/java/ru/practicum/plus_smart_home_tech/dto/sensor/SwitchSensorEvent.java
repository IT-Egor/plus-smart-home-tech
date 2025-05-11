package ru.practicum.plus_smart_home_tech.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    @NotNull(message = "State is required")
    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
