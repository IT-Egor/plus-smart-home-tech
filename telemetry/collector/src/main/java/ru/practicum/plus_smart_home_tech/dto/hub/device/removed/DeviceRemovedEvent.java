package ru.practicum.plus_smart_home_tech.dto.hub.device.removed;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {
    @NotNull(message = "device id is required")
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
