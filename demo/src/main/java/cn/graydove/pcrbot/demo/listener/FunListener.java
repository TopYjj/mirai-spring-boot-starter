package cn.graydove.pcrbot.demo.listener;

import cn.graydove.pcrbot.mirai.annotation.FriendMessageListener;
import cn.graydove.pcrbot.mirai.annotation.GroupMessageListener;
import cn.graydove.pcrbot.mirai.annotation.MessageListener;
import cn.graydove.pcrbot.mirai.annotation.TempleMessageListener;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.hibernate.validator.internal.engine.groups.Group;
import org.springframework.stereotype.Component;

@Component
public class FunListener {

    /**
     * 消息事件可使用注解方式监听，消息事件监听的注解有{GroupMessageListener, FriendMessageListener, TempleMessageListener, MessageListener}
     * 以下四个类型会自动注入
     * MessageChain, MessageEvent, friend(若为好友消息或临时消息), Group(若为群消息)
     */
    @GroupMessageListener("^reply.*")
    public void replay(MessageChain messageChain, Group group, GroupMessageEvent event) {
        String s = messageChain.contentToString();
        event.getSubject().sendMessage("收到：" + s);
    }

    @FriendMessageListener("^reply.*")
    public void replayFriend(MessageChain messageChain, Friend friend, GroupMessageEvent event) {
        String s = messageChain.contentToString();
        event.getSubject().sendMessage("收到：" + s);
    }
}
