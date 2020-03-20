package ru.alfomine.serverapi.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.ServerInfo;
import ru.alfomine.serverapi.api.exception.ScreenshotNotSupportedException;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

import java.util.stream.Collectors;

public class ServerImplSpigot implements IServer {
    @Override
    public String runCommand(String command, String senderName) throws ServerAPIBaseException {
        CommandRunTask task = new CommandRunTask(command, senderName);

        task.runTask(ServerAPISpigot.instance);

        while (task.output == null) {
            try {
                Thread.sleep(0); // Просто нужен какой-нибудь рандомный код, ибо пустой блок просто зависает.
            } catch (InterruptedException ignored) {

            }
        }

        return task.output;
    }

    @Override
    public String getPlayerScreenshot(String nick, String quality) throws ServerAPIBaseException {
        throw new ScreenshotNotSupportedException();
    }

    @Override
    public void sendDiscordMessage(String nick, String message) throws ServerAPIBaseException {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.format("&7-&2G &9| Discord | &6%s&f: %s", nick, message)));
    }

    @Override
    public String[] getPlayerList(boolean publicList) throws ServerAPIBaseException {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()).toArray(new String[]{});
    }

    @Override
    public ServerInfo getServerInfo() throws ServerAPIBaseException {
        int publicPlayerCount = getPlayerList(true).length;
        int maxPlayers = Bukkit.getMaxPlayers();
        long serverUptime = System.currentTimeMillis() - ServerAPISpigot.startTime;
        double serverTPS = 20.1; // Мне серьезно лень ебаться с тпс на баките, на этой залупе просто нет способа получить тпс через API.
        // Да и к афм мы их не подключаем, так что, эта херь все равно нигде не будет выводиться.

        return new ServerInfo(publicPlayerCount, maxPlayers, serverUptime, serverTPS);
    }

    @Override
    public void serverLog(String message) {
        Bukkit.getLogger().info("\u00A7a[ServerAPI] " + message);
    }
}
