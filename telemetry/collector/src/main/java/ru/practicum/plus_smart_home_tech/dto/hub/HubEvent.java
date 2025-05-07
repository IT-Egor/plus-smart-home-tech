package ru.practicum.plus_smart_home_tech.dto.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.plus_smart_home_tech.dto.hub.device.added.DeviceAddedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.device.removed.DeviceRemovedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.ScenarioAddedEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.removed.ScenarioRemovedEvent;

import java.time.Instant;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
public abstract class HubEvent {
    @NotBlank(message = "hubId is required")
    protected String hubId;

    protected Instant timestamp = Instant.now();

    @NotNull(message = "type is required")
    public abstract HubEventType getType();
}
