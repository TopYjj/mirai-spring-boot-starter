package cn.graydove.roboot.mirai.properties;

import lombok.Data;

@Data
public class LogProperties {

    private boolean printEventLog = true;

    private boolean printMiraiBotLog = true;

    private boolean printMiraiNetworkLog = true;
}
