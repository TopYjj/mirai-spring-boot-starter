package cn.graydove.roboot.mirai.annotation;

import cn.graydove.roboot.mirai.enums.MessageType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MessageListener(MessageTypes = {MessageType.TempMessageEvent})
public @interface TempleMessageListener {
    @AliasFor(annotation = MessageListener.class)
    String[] value() default ".*";

    @AliasFor(annotation = MessageListener.class)
    String[] regex() default ".*";
}
