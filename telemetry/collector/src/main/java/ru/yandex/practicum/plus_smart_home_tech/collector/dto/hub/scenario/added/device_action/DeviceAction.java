package ru.yandex.practicum.plus_smart_home_tech.collector.dto.hub.scenario.added.device_action;

import lombok.Data;

@Data
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private int value;
}
