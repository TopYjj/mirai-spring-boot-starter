package cn.graydove.robot.mirai.annotation;

import cn.graydove.robot.mirai.enums.MessageType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageListener {

    @AliasFor("regex")
    String[] value() default ".*";

    @AliasFor("value")
    String[] regex() default ".*";

    MessageType[] MessageTypes() default {MessageType.ALL};
}
