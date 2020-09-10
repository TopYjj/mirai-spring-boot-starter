package cn.graydove.robot.mirai.config;

import cn.graydove.robot.mirai.annotation.MessageListener;
import cn.graydove.robot.mirai.core.function.Function;
import cn.graydove.robot.mirai.core.function.FunctionManager;
import cn.graydove.robot.mirai.core.processor.EventProcessor;
import cn.graydove.robot.mirai.core.processor.EventProcessorManager;
import cn.graydove.robot.mirai.properties.BotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

@Slf4j
@Configuration
@EnableConfigurationProperties(BotProperties.class)
@ConditionalOnProperty(prefix = "bot", name = "enable", havingValue = "true", matchIfMissing = true)
public class ManagerConfiguration implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private EventProcessorManager eventProcessorManager;

    private FunctionManager functionManager;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            MessageListener messageListener = AnnotatedElementUtils.findMergedAnnotation(method, MessageListener.class);
            if (messageListener != null) {
                functionManager.register(new Function(applicationContext, method, bean, messageListener.value(), messageListener.MessageTypes()));
                log.info("register function success");
            }
        }
        if (bean instanceof EventProcessor) {
            eventProcessorManager.registerProcessor((EventProcessor) bean);
            log.info("register eventProcessor success");
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.eventProcessorManager = applicationContext.getBean(EventProcessorManager.class);
        this.functionManager = applicationContext.getBean(FunctionManager.class);
    }
}
