package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import static me.lichris93.jrrp.values.*;

public class jrrp extends JavaPlugin {

    @Override
    public void onEnable() {
        //Make the values not null
        config = this.getConfig();
        instance = this;
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
    public static jrrp getSelf() {
        return instance;
    }
    public void regCommand(){
        try {
            Bukkit.getPluginCommand("jrrp").setExecutor(new gameCommand());
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
            Bukkit.getPluginManager().registerEvents(new groupMsg(), this);
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
            qqBot = config.getLong("bot");
            qqGroup.addAll(config.getLongList("group"));
            admin = config.getString("admin");
            jrrpMes = config.getString("lang.jrrpmes");
            version = config.getString("version");
            jrrpClear = config.getString("lang.jrrpclear");
            sendMap = config.getString("lang.sendmap");
            getFailMes = config.getString("lang.getfailmes");
            getSucceedMes = config.getString("lang.getsucceedmes");
            if (admin.contains(",")) {
                String[] temp = admin.split(",");
                list.addAll(Arrays.asList(temp));
            } else {
                list.add(admin);
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
