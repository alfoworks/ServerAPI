package ru.alfomine.serverapi.sponge;

import eu.crushedpixel.sponge.packetgate.api.registry.PacketConnection;
import eu.crushedpixel.sponge.packetgate.api.registry.PacketGate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.ServerInfo;
import ru.alfomine.serverapi.api.exception.PlayerNotFoundException;
import ru.alfomine.serverapi.api.exception.ScreenshotException;
import ru.alfomine.serverapi.api.exception.ScreenshotTimeoutException;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

import java.io.IOException;
import java.util.*;

public class ServerImplSponge implements IServer {
    @Override
    public String runCommand(String command, String senderName, String groupName) {
        CommandRunTask task = new CommandRunTask(command, senderName, groupName);

        Task.builder().execute(task).name("ServerAPI command run").submit(ServerAPISponge.getPlugin());

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
        if (!Sponge.getServer().getPlayer(nick).isPresent()) {
            throw new PlayerNotFoundException();
        }

        Player player = Sponge.getServer().getPlayer(nick).get();
        String channelName = generateRandomChannelName();

        ChannelBinding.RawDataChannel channel = Sponge.getGame().getChannelRegistrar().createRawChannel(ServerAPISponge.getPlugin(), channelName);
        ScreenshotListener listener = new ScreenshotListener();
        channel.addListener(listener);

        // Отправка пакета //

        PacketGate packetGate = Sponge.getServiceManager().provide(PacketGate.class).orElseThrow(ScreenshotException::new);
        PacketConnection connection = packetGate.connectionByPlayer(player).orElseThrow(ScreenshotException::new);

        ByteBuf byteBuf = Unpooled.buffer(0);
        PacketBuffer buffer = new PacketBuffer(byteBuf);

        int qualityInt;

        switch (quality) {
            case "high":
                qualityInt = 1;
                break;
            case "low":
                qualityInt = 2;
                break;
            default:
                qualityInt = 3;
                break;
        }

        buffer.writeVarInt(1);
        buffer.writeUniqueId(UUID.randomUUID());
        buffer.writeString(String.format("%%%s%s", qualityInt, channelName));
        buffer.writeBlockPos(new BlockPos(0, 0, 0));
        buffer.writeByte(2);

        SPacketSpawnPainting packet = new SPacketSpawnPainting();
        try {
            packet.readPacketData(buffer);
        } catch (IOException e) {
            throw new ScreenshotException();
        }

        connection.sendPacket(packet);

        // =============== //

        long startTime = System.currentTimeMillis();

        while (true) {
            if (listener.ok) {
                break;
            } else {
                if (System.currentTimeMillis() > startTime + 5000) {
                    break;
                } else {
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        throw new ScreenshotException();
                    }
                }
            }
        }

        if (!listener.ok) {
            serverLog(String.format("[screenshot] Failed to take screenshot of player %s: timed out.", player.getName()));

            throw new ScreenshotTimeoutException();
        }

        if (listener.result.length == 0) {
            serverLog(String.format("[screenshot] Failed to take screenshot of player %s: array is empty", player.getName()));

            throw new ScreenshotException();
        }

        channel.removeListener(listener);
        Sponge.getChannelRegistrar().unbindChannel(channel);

        serverLog(String.format("[screenshot] Made screenshot of player %s which took %s packets.", player.getName(), listener.packets));

        return Base64.getEncoder().encodeToString(listener.result);
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
        ServerAPISponge.logger.info("\u00A7a[ServerAPI] " + message);
    }

    // ================================= //

    private String generateRandomChannelName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        int len = new Random().nextInt(10) + 1;

        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int index = (int) (chars.length() * Math.random());

            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
