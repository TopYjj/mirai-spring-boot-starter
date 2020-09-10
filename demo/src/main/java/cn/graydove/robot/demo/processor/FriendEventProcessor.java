package cn.graydove.robot.demo.processor;

import cn.graydove.robot.mirai.core.processor.EventProcessor;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class FriendEventProcessor implements EventProcessor {

    /**
     *
     * @param event mirai事件
     * @return 若返回true则执行此EventProcessor
     */
    @Override
    public boolean accept(Event event) {
        return FriendMessageEvent.class.equals(event.getClass()) || GroupMessageEvent.class.equals(event.getClass());
    }

    /**
     * 具体处理代码
     * @param event mirai事件
     */
    @Override
    public void process(Event event) {
        MessageEvent e = (MessageEvent) event;
        String message = "从" + event.getClass() + "收到消息: " + e.getMessage().contentToString();
        if (e instanceof GroupMessageEvent) {
            ((GroupMessageEvent) e).getGroup().sendMessage(message);
        } else {
            e.getSender().sendMessage(message);
        }
    }
}
