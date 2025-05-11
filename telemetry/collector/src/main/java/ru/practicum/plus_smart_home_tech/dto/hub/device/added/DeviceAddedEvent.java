package ru.practicum.plus_smart_home_tech.dto.hub.device.added;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    @NotNull(message = "device id is required")
    private String id;

    @NotNull(message = "device type is required")
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
