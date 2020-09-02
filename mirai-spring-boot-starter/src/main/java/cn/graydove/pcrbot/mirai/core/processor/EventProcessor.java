package cn.graydove.pcrbot.mirai.core.processor;

import net.mamoe.mirai.event.Event;

public interface EventProcessor {

    boolean accept(Event event);

    void process(Event event);
}
