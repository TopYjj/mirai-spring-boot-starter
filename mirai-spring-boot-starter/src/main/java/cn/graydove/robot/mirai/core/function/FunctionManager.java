package cn.graydove.robot.mirai.core.function;

import net.mamoe.mirai.message.MessageEvent;

import java.util.ArrayList;
import java.util.List;

public class FunctionManager {

    private List<Function> functions = new ArrayList<>();

    public void register(Function function) {
        functions.add(function);
    }

    public boolean handle(MessageEvent event) {
        for (Function function: functions) {
            if (function.match(event)) {
                function.invoke(event);
                return true;
            }
        }
        return false;
    }

}
