package cn.graydove.roboot.mirai.core.function;

import cn.graydove.roboot.mirai.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.MessageEvent;
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
        Type[] types = method.getGenericParameterTypes();
        int size = types.length;
        Object[] params = new Object[size];
        for (int i=0;i<size;++i) {
            Class<?> clazz = (Class<?>) types[i];
            if (MessageEvent.class.isAssignableFrom(clazz)) {
                params[i] = event;
            } else if (Bot.class.isAssignableFrom(clazz)) {
                params[i] = event.getBot();
            } else if (Contact.class.isAssignableFrom(clazz)) {
                params[i] = event.getSubject();
            } else if (MessageChain.class.isAssignableFrom(clazz)) {
                params[i] = event.getMessage();
            } else if (User.class.isAssignableFrom(clazz)) {
                params[i] = event.getSender();
            } else {
                params[i] = applicationContext.getBean(clazz);
            }
        }
        try {
            method.invoke(object, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(method.getName() + "调用失败", e);
        }
    }
}
