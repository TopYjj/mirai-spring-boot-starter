package cn.graydove.robot.mirai.config;

import cn.graydove.robot.mirai.core.BotThread;
import cn.graydove.robot.mirai.core.exception.BotExceptionHandler;
import cn.graydove.robot.mirai.core.exception.DefaultBotExceptionHandler;
import cn.graydove.robot.mirai.core.function.FunctionManager;
import cn.graydove.robot.mirai.core.processor.EventProcessorManager;
import cn.graydove.robot.mirai.exception.NoSuchFileException;
import cn.graydove.robot.mirai.properties.BotProperties;
import cn.graydove.robot.mirai.properties.LogProperties;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(BotProperties.class)
@ConditionalOnProperty(prefix = "bot", name = "enable", havingValue = "true", matchIfMissing = true)
public class MiraiConfiguration {

    private BotProperties properties;

    public MiraiConfiguration(BotProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(Bot.class)
    public Bot bot() {
        String path = Optional.ofNullable(this.getClass().getClassLoader().getResource("device.json"))
                .map(URL::getPath).orElseThrow(NoSuchFileException::new);
        return BotFactoryJvm.newBot(properties.getQq(), properties.getPassword(), new BotConfiguration() {
            {
                fileBasedDeviceInfo(path);
                LogProperties log = properties.getLog();
                if (!log.isPrintMiraiBotLog()) {
                    noBotLog();
                }

                if (!log.isPrintMiraiNetworkLog()) {
                    noNetworkLog();
                }
            }
        });
    }

    @Bean
    public BotThread botThread() {
        BotThread botThread = new BotThread(bot(), functionManager(), eventProcessorManager(), botExceptionHandler());
        botThread.setPrintLog(properties.getLog().isPrintEventLog());
        botThread.start();
        return botThread;
    }

    @Bean
    public EventProcessorManager eventProcessorManager() {
        return new EventProcessorManager();
    }

    @Bean
    public FunctionManager functionManager() {
        return new FunctionManager();
    }

    @Bean
    @ConditionalOnMissingBean(BotExceptionHandler.class)
    public BotExceptionHandler botExceptionHandler() {
        return new DefaultBotExceptionHandler();
    }

}
