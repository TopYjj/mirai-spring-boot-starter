package cn.graydove.pcrbot.mirai.core.exception;

import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;

public class DefaultBotExceptionHandler implements BotExceptionHandler {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }
}
