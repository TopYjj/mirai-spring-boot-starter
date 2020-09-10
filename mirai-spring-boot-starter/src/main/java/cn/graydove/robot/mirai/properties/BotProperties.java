package cn.graydove.robot.mirai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("bot")
public class BotProperties {

    private boolean enable = true;

    @NestedConfigurationProperty
    private LogProperties log = new LogProperties();

    private long qq;

    private String password;
}
