package cn.graydove.pcrbot.demo.config;

import cn.graydove.pcrbot.mirai.core.exception.BotExceptionHandler;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

//    /**
//     * 可自定义Bot实例
//     * @return
//     */
//    @Bean
//    public Bot bot() {
//        return BotFactoryJvm.newBot(123,"password");
//    }

    /**
     * 自定义异常处理器
     */
    @Bean
    public BotExceptionHandler botExceptionHandler() {
        return new SimpleExceptionHandler();
    }
}

@Slf4j
class SimpleExceptionHandler implements BotExceptionHandler {

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        log.error(context.toString(), exception);
    }
}
