package me.lichris93.jrrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static me.lichris93.jrrp.values.*;

public class gamecommand implements CommandExecutor {
    jrrp ins = jrrp.getSelf();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender.isOp()) {
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {//jrrp help
                showHelp(commandSender);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("addadmin")) {//jrrp addadmin <qqid>
                addAdmin(commandSender,args[1]);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("deladmin")) {//jrrp deladmin <qqid>
                delAdmin(commandSender,args[1]);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {//jrrp reload
                reloadConfigYml(commandSender);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("isadmin")) {//jrrp isadmin <qqid>
               isAdmin(commandSender,args[1]);
            } else {
                commandSender.sendMessage("§ajrrp v" + version + "正在这个服务器上运行, 使用 /jrrp help 来获取帮助");
            }
        } else {
            commandSender.sendMessage("§c你没有OP权限,无法执行命令");
        }
        return true;
    }
    public boolean hasPermission(long qqNum) {
        for (String s : list) {
            if (Long.toString(qqNum).equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;

    }
    public void showHelp(@NotNull CommandSender commandSender){
        commandSender.sendMessage("§a--------------[ jrrp ]--------------");
        commandSender.sendMessage("§a/jrrp help              显示本帮助信息");
        commandSender.sendMessage("§a/jrrp reload                 重载配置");
        commandSender.sendMessage("§a/jrrp addadmin <qqid>          加管理");
        commandSender.sendMessage("§a/jrrp deladmin <qqid>          减管理");
        commandSender.sendMessage("§a/jrrp isadmin <qqid>    判断是否是管理");
        commandSender.sendMessage("§a----------[ By LiChris93 ]-----------");
    }
    public void addAdmin(CommandSender commandSender, @NotNull String qqNum){
        if (qqNum.matches("[1-9][0-9]{4,14}")) {
            list.add(qqNum);
            final boolean sta = null != list;//判断list不为null
            List<String> templist = new ArrayList<>();
            if (sta) {
                Set<String> set = new HashSet<>(list);
                templist.addAll(set);
            }
            StringBuilder temp = new StringBuilder();
            if (templist.size() > 1) {
                for (String s : templist) {
                    temp.append(s).append(",");
                }
                temp.deleteCharAt(temp.length()-1);//去掉结尾逗号
            } else {
                temp = new StringBuilder(templist.get(0));
            }
            config.set("admin", temp.toString());
            ins.saveConfigYml();
            commandSender.sendMessage("§a添加完成!" + qqNum);
        } else {
            commandSender.sendMessage("§c不正确的QQ号！");
        }
    }
    public void reloadConfigYml(CommandSender commandSender){
        ins.reloadConfig();
        config = ins.getConfig();
        try {
            list.clear();
            qqbot = config.getLong("bot");
            qqgroup = config.getLong("group");
            admin = config.getString("admin");
            jrrpmes = config.getString("lang.jrrpmes");
            version = config.getString("version");
            jrrpclear = config.getString("lang.jrrpclear");
            sendmap = config.getString("lang.sendmap");
            getfailmes = config.getString("lang.getfailmes");
            getsucceedmes = config.getString("lang.getsucceedmes");
            if (admin.contains(",")) {
                String[] temp = admin.split(",");
                for (String i : temp) {
                    if (i.matches("[1-9][0-9]{4,14}")) {
                        list.add(i);
                    } else {
                        ins.warn(i + "不是有效的qq号");
                    }
                }
            } else {
                if (admin.matches("[1-9][0-9]{4,14}")) {
                    list.add(admin);
                } else {
                    ins.warn(admin + "不是有效的qq号");
                }
            }
            commandSender.sendMessage("§aconfig重载完成");
        } catch (Exception e) {
            ins.info(e.toString());

            commandSender.sendMessage("§cconfig重载失败，详细信息查看控制台");
        }
    }
    public void isAdmin(CommandSender commandSender,String qqNum){
        if (hasPermission(Long.parseLong(qqNum))) {
            commandSender.sendMessage("§a该用户是管理");
        } else {
            commandSender.sendMessage("§c该用户不是管理");
        }
    }
    public void delAdmin(CommandSender commandSender,String qqNum){
        if (hasPermission(Long.parseLong(qqNum))) {
            list.remove(qqNum);
            final boolean sta = null != list && list.size() > 0;//判断list不为null
            //set转list用list方法
            List<String> templist = new ArrayList<>();
            if (sta) {
                Set<String> set = new HashSet<>(list);
                templist.addAll(set);
            }
            StringBuilder temp = new StringBuilder();
            if (templist.size() > 1) {
                for (String s : templist) {
                    temp.append(s).append(",");
                }
                temp.deleteCharAt(temp.length()-1);//去掉结尾逗号
            } else {
                temp = new StringBuilder(templist.get(0));
            }
            config.set("admin", temp.toString());
            ins.saveConfigYml();
            commandSender.sendMessage("§a移除完成!" + qqNum);
        } else if (!qqNum.matches("[1-9][0-9]{4,14}")) {
            commandSender.sendMessage("§c不正确的QQ号！");
        } else {
            commandSender.sendMessage("§c该用户不是管理！");
        }
    }
}
