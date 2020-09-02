package cn.graydove.pcrbot.mirai.core.exception;

import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;

public interface BotExceptionHandler {


    void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception);

}
