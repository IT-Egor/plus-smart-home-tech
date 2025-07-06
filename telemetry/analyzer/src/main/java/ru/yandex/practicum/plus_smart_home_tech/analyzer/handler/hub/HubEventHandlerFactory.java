package ru.yandex.practicum.plus_smart_home_tech.analyzer.handler.hub;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HubEventHandlerFactory {
    private final Map<Class<?>, HubEventHandler> handlers = new HashMap<>();

    public HubEventHandlerFactory(List<HubEventHandler> handlers) {
        for (HubEventHandler handler : handlers) {
            this.handlers.put(handler.getPayloadClass(), handler);
        }
    }

    public HubEventHandler getHandler(Class<?> payloadClass) {
        if (handlers.containsKey(payloadClass)) {
            return handlers.get(payloadClass);
        } else {
            throw new IllegalArgumentException("No handler for payload class: " + payloadClass);
        }
    }
}
