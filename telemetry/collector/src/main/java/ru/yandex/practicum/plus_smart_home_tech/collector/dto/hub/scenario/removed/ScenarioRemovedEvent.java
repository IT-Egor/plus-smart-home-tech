package ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.scenario.removed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEvent;
import ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.HubEventType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
