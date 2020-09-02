package cn.graydove.pcrbot.mirai.core;

import cn.graydove.pcrbot.mirai.core.exception.BotExceptionHandler;
import cn.graydove.pcrbot.mirai.core.function.FunctionManager;
import cn.graydove.pcrbot.mirai.core.processor.EventProcessorManager;
import cn.graydove.pcrbot.mirai.exception.CloseException;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PreDestroy;


@Slf4j
public class BotThread extends Thread {

    private final Bot bot;

    private final FunctionManager functionManager;

    private final EventProcessorManager eventProcessorManager;

    private final BotExceptionHandler exceptionHandler;

    private volatile boolean printLog = true;

    public BotThread(Bot bot, FunctionManager functionManager, EventProcessorManager eventProcessorManager, BotExceptionHandler exceptionHandler) {
        super("Mirai Bot");
        this.bot = bot;
        this.functionManager = functionManager;
        this.eventProcessorManager = eventProcessorManager;
        this.exceptionHandler = exceptionHandler;
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public boolean isPrintLog() {
        return printLog;
    }

    public Bot getBot() {
        return bot;
    }

    @PreDestroy
    public void destroy() {
        bot.close(new CloseException());
    }

    @Override
    public void run() {
        log.info("bot login...");
        bot.login();
        log.info("bot login success");

        Events.registerEvents(bot, new SimpleListenerHost(){

            @EventHandler
            public ListeningStatus listen(Event event) {
                if (printLog)
                    log(event);
                boolean deal = false;
                if (event instanceof MessageEvent)
                    deal = functionManager.handle((MessageEvent) event);

                if (!deal)
                    eventProcessorManager.process(event);
                return ListeningStatus.LISTENING;
            }

            //处理在处理事件中发生的未捕获异常
            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                exceptionHandler.handleException(context, exception);
            }
        });

        log.info("bot start finished");
        bot.join();
    }


    private void log(Event event) {
        if (event instanceof NewFriendRequestEvent)
            log.info("new friend request[from QQ:{}]", ((NewFriendRequestEvent) event).getFromId());
        else if (event instanceof GroupMessageEvent) {
            Group group = ((GroupMessageEvent) event).getGroup();
            MessageChain messages = ((GroupMessageEvent) event).getMessage();
            log.info("GroupMessage[{}({})]: {}", group.getName(), group.getId(), messages.contentToString());
        } else if (event instanceof FriendMessageEvent) {
            Friend sender = ((FriendMessageEvent) event).getSender();
            MessageChain messages = ((FriendMessageEvent) event).getMessage();
            log.info("FriendMessage[{}({})]: {}", sender.getNick(), sender.getId(), messages.contentToString());
        } else {
            log.info("other event: {}", event.getClass());
        }
    }
}
