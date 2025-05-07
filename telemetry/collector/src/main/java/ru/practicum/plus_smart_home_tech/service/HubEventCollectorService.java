package ru.practicum.plus_smart_home_tech.service;

import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;

public interface HubEventCollectorService {
    void collect(HubEvent hubEvent);
}
