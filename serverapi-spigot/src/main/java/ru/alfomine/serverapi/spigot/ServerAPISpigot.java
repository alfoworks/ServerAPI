package ru.alfomine.serverapi.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.alfomine.serverapi.server.HTTPServer;

public final class ServerAPISpigot extends JavaPlugin {
    public static long startTime = 0;
    public static ServerAPISpigot instance;
    public HTTPServer server = null;
    public int serverTaskId = 0;
    public ServerImplSpigot serverImpl = new ServerImplSpigot();

    @Override
    public void onEnable() {
        instance = this;
        startTime = System.currentTimeMillis();

        int port = this.getConfig().getInt("serverApiPort");

        if (port == 0) {
            serverImpl.serverLog("Can't start ServerAPI server: port not set.");
            return;
        }

        server = new HTTPServer(port, serverImpl);
        serverTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                server.run();
            }
        }.runTaskAsynchronously(this).getTaskId();
    }

    @Override
    public void onDisable() {
        if (server == null || serverTaskId == 0) return;

        Bukkit.getScheduler().cancelTask(serverTaskId);

        server = null;
        serverTaskId = 0;
    }
}
