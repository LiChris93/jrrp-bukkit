package me.lichris93.jrrp.Utils;
import me.lichris93.jrrp.jrrp;


public class ServerUtils {
    public static void runAsync(Runnable runnable) {
        jrrp.getself().getServer().getScheduler().runTaskAsynchronously(jrrp.getself(), runnable);
    }
}
