package cn.graydove.roboot.mirai.core.function;

import cn.graydove.roboot.mirai.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

@Slf4j
public class Function {
    private Method method;

    private Object object;

    private Pattern[] regex;

    private MessageType[] messageTypes;

    public Function(Method method, Object object, String[] regex, MessageType[] messageTypes) {
        this.method = method;
        this.object = object;
        this.messageTypes = messageTypes;
        int len = regex.length;
        this.regex = new Pattern[len];
        for (int i=0; i<len; ++i) {
            this.regex[i] = Pattern.compile(regex[i]);
        }
    }

    public boolean match(MessageEvent event) {
        String text = event.getMessage().contentToString();
        for (Pattern p: regex) {
            if (matchEvent(event.getClass()) && p.matcher(text).find()) {
                return true;
            }
        }
        return false;
    }

    private boolean matchEvent(Class<? extends MessageEvent> event) {
        for (MessageType messageType: messageTypes) {
            if (messageType == MessageType.ALL) {
                return true;
            } else {
                if (messageType.getEventClass().equals(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void invoke(MessageEvent event) {
        Type[] types = method.getGenericParameterTypes();
        int size = types.length;
        Object[] params = new Object[size];
        for (int i=0;i<size;++i) {
            if (Event.class.isAssignableFrom((Class<?>) types[i])) {
                params[i] = event;
            } else if (Group.class.equals(types[i])) {
                if (event instanceof GroupMessageEvent) {
                    params[i] = ((GroupMessageEvent) event).getGroup();
                }
            } else if (Friend.class.equals(types[i])) {
                if (event instanceof FriendMessageEvent) {
                    params[i] = ((FriendMessageEvent) event).getSender();
                }
            } else if (((Class<?>) types[i]).isAssignableFrom(MessageChain.class)) {
                params[i] = event.getMessage();
            }
        }
        try {
            method.invoke(object, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("方法调用失败");
        }
    }
}
