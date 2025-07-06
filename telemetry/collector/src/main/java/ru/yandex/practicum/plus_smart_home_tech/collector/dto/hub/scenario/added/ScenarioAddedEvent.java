package ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.scenario.added;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEvent;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEventType;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.scenario.added.device_action.DeviceAction;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.scenario.added.condition.ScenarioCondition;

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
