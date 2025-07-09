package ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.device.removed;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEvent;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEventType;

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
