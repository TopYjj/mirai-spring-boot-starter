package cn.graydove.roboot.demo.listener;

import cn.graydove.roboot.mirai.annotation.FriendMessageListener;
import cn.graydove.roboot.mirai.annotation.GroupMessageListener;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.hibernate.validator.internal.engine.groups.Group;
import org.springframework.stereotype.Component;

@Component
public class FunListener {

    /**
     * 消息事件可使用注解方式监听，消息事件监听的注解有{GroupMessageListener, FriendMessageListener, TempleMessageListener, MessageListener}
     * 以下四个类型会自动注入
     * MessageChain, MessageEvent, Contact, User, Bot, 以及其他springApplication管理的Bean
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
