package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class jrrp extends JavaPlugin {

    @Override
    public void onEnable() {
        //Make the values not null
        values.config = this.getConfig();
        values.instance = this;
        //Create default yml file when missing
        saveDefaultConfig();
        //Begin Enabling
        //Record how much time does enable this plugin use
        long loadtime = System.currentTimeMillis();
        info("jrrp is now enabling ——By LiChris93");
        //Register bot event
        regEvent();
        //Register game command
        regCommand();
        //Load config.yml
        loadConfig();
        //Finish Enabling
        info("jrrp 加载完成！——By LiChris93[" + (System.currentTimeMillis() - loadtime) + "ms]");

    }
    @Override
    public void onDisable() {
        //Save config.yml when disabling
        saveConfigYml();
        //Finish Disabling
        info("jrrp 卸载完毕！——By LiChris93");
    }
    public static jrrp getself() {
        return values.instance;
    }
    public void regCommand(){
        try {
            Bukkit.getPluginCommand("jrrp").setExecutor(new gamecommand());
            info("命令执行器注册完成");
        }catch (Exception e){
            warn("jrrp加载失败！原因：命令执行器注册失败，详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }
    public void regEvent(){
        try {
            Bukkit.getPluginManager().registerEvents(new groupmsg(), this);
            info("事件监听器注册完成");
        }catch (Exception e){
            warn("jrrp加载失败！原因：事件监听器注册失败，详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }
    public void loadConfig() {
        try {
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
            info("config读取完成");
        }catch (Exception e){
            warn("jrrp加载失败！原因：config读取失败，详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }

    }
    public void saveConfigYml(){
        try {
            saveConfig();
            info("config已保存");
        }catch (Exception e){
            warn("config保存失败，详情请看控制台");
            e.printStackTrace();
        }
    }
    public void info(String text){
        getLogger().info(text);
    }
    public void warn(String text){
        getLogger().warning(text);
    }

}
