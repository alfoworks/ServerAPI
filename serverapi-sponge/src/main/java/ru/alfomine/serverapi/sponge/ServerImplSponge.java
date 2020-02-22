package ru.alfomine.serverapi.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.ServerInfo;
import ru.alfomine.serverapi.api.exception.ScreenshotNotSupportedException;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

import java.util.ArrayList;
import java.util.List;

public class ServerImplSponge implements IServer {

    @Override
    public String runCommand(String command) {
        CommandRunTask task = new CommandRunTask(command);

        Task.builder().execute(task).name("ServerAPI command run").submit(Sponge.getPluginManager().getPlugin("serverapi").get().getInstance().get());

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
    public void sendDiscordMessage(String nick, String message) {
        Sponge.getServer().getBroadcastChannel().send(TextSerializers.FORMATTING_CODE.deserialize(String.format("&7-&2G &9| Discord | &6%s&f: %s", nick, message)));
    }

    @Override
    public String[] getPlayerList(boolean publicList) {
        List<String> players = new ArrayList<>();

        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            if (publicList && player.get(Keys.VANISH).isPresent() && player.get(Keys.VANISH).get()) {
                continue;
            }

            players.add(player.getName());
        }

        return players.toArray(new String[]{});
    }

    @Override
    public ServerInfo getServerInfo() {
        int publicPlayerCount = getPlayerList(true).length;
        int maxPlayers = Sponge.getServer().getMaxPlayers();
        long serverUptime = System.currentTimeMillis() - ServerAPISponge.startTime;
        double serverTPS = Sponge.getServer().getTicksPerSecond();

        return new ServerInfo(publicPlayerCount, maxPlayers, serverUptime, serverTPS);
    }

    @Override
    public void serverLog(String message) {
        ServerAPISponge.logger.info(message);
    }
}
