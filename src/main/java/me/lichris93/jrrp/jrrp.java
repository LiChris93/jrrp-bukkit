package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class jrrp extends JavaPlugin {
    long loadtime;

    @Override
    public void onEnable() {
        values.config = this.getConfig();
        values.instance = this;
        saveDefaultConfig();
        loadtime = System.currentTimeMillis();
        getLogger().info("-------------------------------------");
        getLogger().info(" L      IIIII   999999999  333333333 ");
        getLogger().info(" L        I     9       9          3 ");
        getLogger().info(" L        I     9       9          3 ");
        getLogger().info(" L        I     999999999  333333333 ");
        getLogger().info(" L        I             9          3 ");
        getLogger().info(" L        I             9          3");
        getLogger().info(" L        I     9       9          3");
        getLogger().info(" LLLLLL IIIII   999999999  333333333 ");
        getLogger().info("-------------------------------------");
        Bukkit.getPluginManager().registerEvents(new groupmsg(), this);
        getLogger().info("事件监听器注册完成");
        Bukkit.getPluginCommand("jrrp").setExecutor(new gamecommand());
        getLogger().info("命令执行器注册完成");
        values.qqbot = values.config.getLong("bot");
        values.qqgroup = values.config.getLong("group");
        values.admin = values.config.getString("admin");
        values.jrrpmes = values.config.getString("lang.jrrpmes");
        values.version = values.config.getString("version");
        values.jrrpclear = values.config.getString("lang.jrrpclear");
        values.sendmap = values.config.getString("lang.sendmap");
        values.getfailmes = values.config.getString("lang.getfailmes");
        values.getsucceedmes = values.config.getString("lang.getsucceedmes");
        if (values.admin.contains(",")) {
            String[] temp = values.admin.split(",");
            values.list.addAll(Arrays.asList(temp));
        } else {
            values.list.add(values.admin);
        }
        getLogger().info("config读取完成");
        getLogger().info("jrrp 加载完成！——By LiChris93[" + (System.currentTimeMillis() - loadtime) + "ms]");

    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("config已保存");
        getLogger().info("jrrp 卸载完毕！——By LiChris93");
    }

    public static jrrp getself() {
        return values.instance;
    }

    public static void main(String[] args) {


    }
}
