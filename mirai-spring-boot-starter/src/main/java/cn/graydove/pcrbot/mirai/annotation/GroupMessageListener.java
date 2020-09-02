package cn.graydove.pcrbot.mirai.annotation;

import cn.graydove.pcrbot.mirai.enums.MessageType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MessageListener(MessageTypes = {MessageType.GroupMessageEvent})
public @interface GroupMessageListener {

    @AliasFor(annotation = MessageListener.class)
    String[] value() default ".*";

    @AliasFor(annotation = MessageListener.class)
    String[] regex() default ".*";
}
