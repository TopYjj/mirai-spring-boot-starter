package cn.graydove.robot.mirai.annotation;

import cn.graydove.robot.mirai.enums.MessageType;
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
