package ru.practicum.plus_smart_home_tech.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.plus_smart_home_tech.dto.hub.HubEvent;
import ru.practicum.plus_smart_home_tech.dto.sensor.SensorEvent;
import ru.practicum.plus_smart_home_tech.service.HubEventCollectorService;
import ru.practicum.plus_smart_home_tech.service.SensorEventCollectorService;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class CollectorController {
    private final SensorEventCollectorService sensorEventCollectorService;
    private final HubEventCollectorService hubEventCollectorService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        sensorEventCollectorService.collect(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        hubEventCollectorService.collect(event);
    }
}

