package ru.yandex.practicum.plus_smart_home_tech.analyzer.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessorRunner implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;

    @Override
    public void run(String... args) {
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();
//        snapshotProcessor.start();
    }
}
