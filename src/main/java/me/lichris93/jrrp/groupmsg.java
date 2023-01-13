package me.lichris93.jrrp;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.lichris93.jrrp.Utils.ServerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class groupmsg implements Listener {
    @EventHandler
    public void onsend(MiraiGroupMessageEvent e) {
        if (e.getGroupID() != values.qqgroup) {
            return;
        }//如果不是来自配置中的群就跳出
        if (e.getMessage().equals(values.jrrpmes)) {
            if (values.Time.get(e.getSenderID()) == null) {// 判断hashmap中是否存在发送者，没有则发送消息并存储

                Date now = new Date();
                SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");// 格式化当天的日期

                String num = Integer.toString(new Random().nextInt(101));
                send(values.getsucceedmes.replaceAll("%sendername%", e.getSenderName()).replaceAll("%rpnum%", num));// 发送消息
                String[] temp = {f.format(now), num};
                values.Time.put(e.getSenderID(), temp);// 放置到hashmap中
            } else {// 如果hashmap中存在
                Date now = new Date();
                SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
                if (!f.format(now).equals(values.Time.get(e.getSenderID())[0])) {// 如果不是当天再次发送
                    String num = Integer.toString(new Random().nextInt(101));
                    send(values.getsucceedmes.replaceAll("%sendername%", e.getSenderName()).replaceAll("%rpnum%", num));// 发送消息
                    String[] temp = {f.format(now), num};
                    values.Time.put(e.getSenderID(), temp);// 重置时间
                } else {// 如果是当天再次发送
                    send(values.getfailmes.replaceAll("%sendername%", e.getSenderName())
                            .replaceAll("%rpnum%", values.Time.get(e.getSenderID())[1]));// 发送还在冷却中消息
                }
            }
        }
        if (e.getMessage().equals(values.jrrpclear)) {
            if (haspermission(e.getSenderID())) {
                values.Time.clear();
                send("HashMap Cleared");
            } else {
                send("你没有权限");
            }
        }
        if (e.getMessage().equals(values.sendmap)) {
            if (haspermission(e.getSenderID())) {
                StringBuilder result = new StringBuilder("{");
                for (Map.Entry<Long, String[]> entry : values.Time.entrySet()) {
                    result.append(entry.getKey()).append("=[").append(entry.getValue()[0]).append(",").append(entry.getValue()[1]).append("]");
                }
                result.append("}");
                send(result.toString());
            } else {
                send("你没有权限");
            }
        }
    }

    public boolean haspermission(long qqnum) {
        for (String s : values.list) {
            if (Long.toString(qqnum).equals(s)) {
                return true;
            }
        }
        return false;

    }

    public void send(String text) {
        ServerUtils.runAsync(() -> {
            MiraiBot.getBot(values.qqbot).getGroup(values.qqgroup).sendMessage(text);
        });
    }
}
