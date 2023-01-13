package me.lichris93.jrrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class gamecommand implements CommandExecutor {
    jrrp ins = jrrp.getself();

    @Override

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender.isOp()) {
            if (args.length == 1 && args[0].equals("help")) {
                commandSender.sendMessage("§a--------------[ jrrp ]--------------");
                commandSender.sendMessage("§a/jrrp help              显示本帮助信息");
                commandSender.sendMessage("§a/jrrp reload                 重载配置");
                commandSender.sendMessage("§a/jrrp addadmin <qqid>          加管理");
                commandSender.sendMessage("§a/jrrp deladmin <qqid>          减管理");
                commandSender.sendMessage("§a/jrrp isadmin <qqid>    判断是否是管理");
                commandSender.sendMessage("§a----------[ By LiChris93 ]-----------");
                return true;
            } else if (args.length == 2 && args[0].equals("addadmin")) {
                if (args[1].matches("[1-9][0-9]{4,14}")) {
                    values.list.add(args[1]);
                    final boolean sta = null != values.list;
                    List<String> templist = new ArrayList<String>();
                    if (sta) {
                        Set<String> set = new HashSet<String>(values.list);
                        templist.addAll(set);
                    }
                    StringBuilder temp = new StringBuilder();
                    if (templist.size() > 1) {
                        for (String s : templist) {
                            temp.append(s).append(",");
                        }
                    } else {
                        temp = new StringBuilder(templist.get(0));
                    }
                    values.config.set("admin", temp.toString());
                    ins.saveConfig();
                    commandSender.sendMessage("§a添加完成!" + args[1]);
                    return true;
                } else {
                    commandSender.sendMessage("§c不正确的QQ号！");
                    return true;
                }
            } else if (args.length == 2 && args[0].equals("deladmin")) {
                if (haspermission(Long.parseLong(args[1]))) {
                    values.list.remove(args[1]);
                    final boolean sta = null != values.list && values.list.size() > 0;//set转list用list方法
                    List<String> templist = new ArrayList<String>();
                    if (sta) {
                        Set<String> set = new HashSet<String>(values.list);
                        templist.addAll(set);
                    }
                    StringBuilder temp = new StringBuilder();
                    if (templist.size() > 1) {
                        for (String s : templist) {
                            temp.append(s).append(",");
                        }
                    } else {
                        temp = new StringBuilder(templist.get(0));
                    }
                    values.config.set("admin", temp.toString());
                    ins.saveConfig();
                    commandSender.sendMessage("§a移除完成!" + args[1]);
                    return true;
                } else if (!args[1].matches("[1-9][0-9]{4,14}")) {
                    commandSender.sendMessage("§c不正确的QQ号！");
                    return true;
                } else {
                    commandSender.sendMessage("§c该用户不是管理！");
                    return true;
                }
            } else if (args.length == 1 && args[0].equals("reload")) {
                try {
                    values.list.clear();
                    values.qqbot = values.config.getLong("bot");
                    values.qqgroup = values.config.getLong("group");
                    values.admin = values.config.getString("admin");
                    values.version = values.config.getString("version");
                    if (values.admin.contains(",")) {
                        String[] temp = values.admin.split(",");
                        for (String i : temp) {
                            if (i.matches("[1-9][0-9]{4,14}")) {
                                values.list.add(i);
                            } else {
                                ins.getLogger().warning(i + "不是有效的qq号");
                            }
                        }
                    } else {
                        if (values.admin.matches("[1-9][0-9]{4,14}")) {
                            values.list.add(values.admin);
                        } else {
                            ins.getLogger().warning(values.admin + "不是有效的qq号");
                        }
                    }
                    commandSender.sendMessage("§aconfig重载完成");
                    return true;
                } catch (Exception e) {
                    ins.getLogger().info(e.toString());

                    commandSender.sendMessage("§cconfig重载失败，详细信息查看控制台");
                    return true;
                }
            } else if (args.length == 2 && args[0].equals("isadmin")) {
                if (haspermission(Long.parseLong(args[1]))) {
                    commandSender.sendMessage("§a该用户是管理");
                } else {
                    commandSender.sendMessage("§c该用户不是管理");
                }
                return true;
            } else {
                commandSender.sendMessage("§ajrrp v" + values.version + "正在这个服务器上运行, 使用 /jrrp help 来获取帮助");
                return true;
            }

        } else {
            commandSender.sendMessage("§c你没有OP权限,无法执行命令");
            return true;
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
}
