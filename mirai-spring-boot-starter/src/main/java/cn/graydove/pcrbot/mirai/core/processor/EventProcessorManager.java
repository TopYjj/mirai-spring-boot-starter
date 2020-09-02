package cn.graydove.pcrbot.mirai.core.processor;

import net.mamoe.mirai.event.Event;

import java.util.ArrayList;
import java.util.List;

public class EventProcessorManager {

    private List<EventProcessor> eventProcessors = new ArrayList<>();

    public void registerProcessor(EventProcessor eventProcessor) {
        eventProcessors.add(eventProcessor);
    }

    public void process(Event event) {
        for (EventProcessor eventProcessor : eventProcessors) {
            if (eventProcessor.accept(event)) {
                eventProcessor.process(event);
                break;
            }
        }
    }
}
