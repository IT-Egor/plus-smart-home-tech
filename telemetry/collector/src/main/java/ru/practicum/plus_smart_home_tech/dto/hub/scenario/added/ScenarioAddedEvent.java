package ru.practicum.plus_smart_home_tech.dto.hub.scenario.added;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEventType;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.device_action.DeviceAction;
import ru.practicum.plus_smart_home_tech.dto.hub.scenario.added.condition.ScenarioCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank(message = "Scenario name is required")
    private String name;

    @NotNull(message = "Scenario conditions are required")
    private List<ScenarioCondition> conditions;

    @NotNull(message = "Scenario actions are required")
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
