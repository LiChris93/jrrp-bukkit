package me.lichris93.jrrp;

import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.lichris93.jrrp.Utils.ServerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

import static me.lichris93.jrrp.values.*;
import static  me.lichris93.jrrp.jrrp.*;
public class groupMsg implements Listener {
    @EventHandler
    public void onSend(@NotNull MiraiGroupMessageEvent e) {
        boolean GroupIsInConfig = false;
        for (Long l : qqGroup){
            if(l == e.getGroupID()){
                GroupIsInConfig = true;
            }
        }
        if(!GroupIsInConfig){//如果不是来自配置中的群就跳出
            return;
        }
        if (e.getMessage().equals(jrrpMes)) {//处理.jrrp
            jrrpMes(e);
        }
        if (e.getMessage().equals(jrrpClear)) {//处理.jrrp-clear
            jrrpClear(e);
        }
        if (e.getMessage().equals(sendMap)) {//处理.jrrp-send-map
            sendMap(e);
        }
        if (e.getMessage().equals(sendRank)){//处理.jrrp-rank
            sendRank(e);
        }
    }



    public void send(String text,MiraiGroupMessageEvent e) {
        ServerUtils.runAsync(() -> e.getGroup().sendMessage(text));
    }
    public void jrrpMes(@NotNull MiraiGroupMessageEvent e){
        if (Time.get(e.getSenderID()) == null) {// 判断hashmap中是否存在发送者，没有则发送消息并存储

            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");// 格式化当天的日期

            String num = Integer.toString(new Random().nextInt(101));
            send(getSucceedMes.replace("%sendername%", e.getSenderName()).replace("%rpnum%", num),e);// 发送消息
            String[] temp = {f.format(now), num,e.getSenderName()};//保存当天日期，人品值和发送者昵称
            Time.put(e.getSenderID(), temp);// 放置到hashmap中
        } else {// 如果hashmap中存在
            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
            if (!f.format(now).equals(Time.get(e.getSenderID())[0])) {// 如果不是当天再次发送
                String num = Integer.toString(new Random().nextInt(101));
                send(getSucceedMes.replace("%sendername%", e.getSenderName()).replace("%rpnum%", num),e);// 发送消息
                String[] temp = {f.format(now), num,e.getSenderName()};//保存当天日期，人品值和发送者昵称
                Time.put(e.getSenderID(), temp);// 重置时间
            } else {// 如果是当天再次发送
                send(getFailMes.replace("%sendername%", e.getSenderName())
                        .replace("%rpnum%", Time.get(e.getSenderID())[1]),e);// 发送还在冷却中消息
            }
        }
    }
    public void jrrpClear(@NotNull MiraiGroupMessageEvent e){
        if (hasAdminPermission(e.getSenderID())) {
            Time.clear();
            send("HashMap Cleared",e);
        } else {
            send("你没有权限",e);
        }
    }
    public void sendMap(@NotNull MiraiGroupMessageEvent e){
        if (hasAdminPermission(e.getSenderID())) {
            StringBuilder result = new StringBuilder("{");
            for (Map.Entry<Long, String[]> entry : Time.entrySet()) {
                result.append(entry.getKey()).append("(").append(entry.getValue()[2]).append(")").append("=[").append(entry.getValue()[0]).append(",").append(entry.getValue()[1]).append("]").append(",");
            }//eg:{1564722665(残影)=[114年5月14日,14]}
            if(result.length()!=1){//排除无人情况
                result.deleteCharAt(result.length()-1);//去掉结尾逗号
            }else{
                result.append(" No data found,:( ");
            }
            result.append("}");
            send(result.toString(),e);
        } else {
            send("你没有权限",e);
        }
    }
    public void sendRank(@NotNull MiraiGroupMessageEvent e){
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
        String date = f.format(now);
        HashMap<String,Long> Rank = new HashMap<>();//得到Rank的未排序Map,保存人品值和QQ号 Rank:String(Key)->luckNum Long(Value)->qqNum
        for(Map.Entry<Long, String[]> entry : Time.entrySet()){
            if(entry.getValue()[0].equals(date)){
                Rank.put(entry.getValue()[1],entry.getKey());//别搞混！这里只是为了排序！TreeMap是根据Key排序的，只能无奈颠倒K和V
            }
        }
        TreeMap<String,Long> RankSorted = new TreeMap<>(Rank);//用TreeMap排序得到已排序的RankSorted
        StringBuilder result = new StringBuilder(date+"的今日人品榜单");
        for(Map.Entry<String,Long> entry : RankSorted.entrySet()){
            result.append("\n").append(Time.get(entry.getValue())[2]).append(":").append(entry.getKey());//输出
        }
        e.getGroup().sendMessage(result.toString());
    }
}
