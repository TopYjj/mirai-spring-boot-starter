package cn.graydove.roboot.mirai.enums;

import net.mamoe.mirai.message.MessageEvent;

public enum MessageType {

    GroupMessageEvent(net.mamoe.mirai.message.GroupMessageEvent.class),
    FriendMessageEvent(net.mamoe.mirai.message.FriendMessageEvent.class),
    TempMessageEvent(net.mamoe.mirai.message.TempMessageEvent.class),
    ALL(MessageEvent.class);

    private final Class<? extends MessageEvent> eventClass;

    MessageType(Class<? extends MessageEvent> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends MessageEvent> getEventClass() {
        return this.eventClass;
    }
}
