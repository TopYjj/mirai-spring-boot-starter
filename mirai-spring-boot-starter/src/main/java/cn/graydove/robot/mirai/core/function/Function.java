package cn.graydove.robot.mirai.core.function;

import cn.graydove.robot.mirai.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.context.ApplicationContext;

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

    private ApplicationContext applicationContext;

    public Function(ApplicationContext applicationContext, Method method, Object object, String[] regex, MessageType[] messageTypes) {
        this.applicationContext = applicationContext;
        this.method = method;
        this.object = object;
        this.messageTypes = messageTypes;
        int len = regex.length;
        this.regex = new Pattern[len];
        for (int i=0; i<len; ++i) {
            this.regex[i] = Pattern.compile(regex[i]);
        }
    }

    boolean match(MessageEvent event) {
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

    void invoke(MessageEvent event) {
        Object[] params = getParam(event);

        try {
            method.invoke(object, params);
        } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
            log.error("方法：" + method.getName() + " 调用失败", e);
        }
    }

    private Object[] getParam(MessageEvent e) {
        Type[] types = method.getGenericParameterTypes();
        int size = types.length;
        Object[] params = new Object[size];

        if (e instanceof GroupMessageEvent) {
            getGroupMessageEventParam((GroupMessageEvent) e, params, types);
        } else if (e instanceof TempMessageEvent) {
            getTempMessageEventParam((TempMessageEvent) e, params, types);
        } else if (e instanceof FriendMessageEvent) {
            getFriendMessageEventParam((FriendMessageEvent) e, params, types);
        }
        return params;
    }

    private void getGroupMessageEventParam(GroupMessageEvent e, Object[] params, Type[] types) {
        for (int i=0;i<params.length;++i) {
            Class<?> clazz = (Class<?>) types[i];
            if (clazz.isAssignableFrom(GroupMessageEvent.class)) {
                params[i] = e;
            } else if (Bot.class.isAssignableFrom(clazz)) {
                params[i] = e.getBot();
            } else if (Group.class.isAssignableFrom(clazz)) {
                params[i] = e.getGroup();
            } else if (MessageChain.class.isAssignableFrom(clazz)) {
                params[i] = e.getMessage();
            } else if (Member.class.isAssignableFrom(clazz)) {
                params[i] = e.getSender();
            } else if (MemberPermission.class.isAssignableFrom(clazz)) {
                params[i] = e.getPermission();
            } else {
                Object o = null;
                try {
                    o = applicationContext.getBean(clazz);
                } catch (Exception ignored) { }
                params[i] = o;
            }
        }
    }
    private void getTempMessageEventParam(TempMessageEvent e, Object[] params, Type[] types) {
        for (int i=0;i<params.length;++i) {
            Class<?> clazz = (Class<?>) types[i];
            if (clazz.isAssignableFrom(TempMessageEvent.class)) {
                params[i] = e;
            } else if (Bot.class.isAssignableFrom(clazz)) {
                params[i] = e.getBot();
            } else if (Group.class.isAssignableFrom(clazz)) {
                params[i] = e.getGroup();
            } else if (MessageChain.class.isAssignableFrom(clazz)) {
                params[i] = e.getMessage();
            } else if (Member.class.isAssignableFrom(clazz)) {
                params[i] = e.getSender();
            } else {
                Object o = null;
                try {
                    o = applicationContext.getBean(clazz);
                } catch (Exception ignored) { }
                params[i] = o;
            }
        }
    }
    private void getFriendMessageEventParam(FriendMessageEvent e, Object[] params, Type[] types) {
        for (int i=0;i<params.length;++i) {
            Class<?> clazz = (Class<?>) types[i];
            if (clazz.isAssignableFrom(FriendMessageEvent.class)) {
                params[i] = e;
            } else if (Bot.class.isAssignableFrom(clazz)) {
                params[i] = e.getBot();
            } else if (MessageChain.class.isAssignableFrom(clazz)) {
                params[i] = e.getMessage();
            } else if (Friend.class.isAssignableFrom(clazz)) {
                params[i] = e.getSender();
            } else {
                Object o = null;
                try {
                    o = applicationContext.getBean(clazz);
                } catch (Exception ignored) { }
                params[i] = o;
            }
        }
    }
}
