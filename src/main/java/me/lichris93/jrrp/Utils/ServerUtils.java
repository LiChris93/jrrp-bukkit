package me.lichris93.jrrp.Utils;
import me.lichris93.jrrp.jrrp;

public class ServerUtils {
    public static void runAsync(Runnable runnable) {
        jrrp.getSelf().getServer().getScheduler().runTaskAsynchronously(jrrp.getSelf(), runnable);
    }
}
