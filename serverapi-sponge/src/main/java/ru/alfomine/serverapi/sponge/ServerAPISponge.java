package ru.alfomine.serverapi.sponge;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import ru.alfomine.serverapi.server.HTTPServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "serverapi",
        name = "ServerAPI"
)
public class ServerAPISponge {
    public static HTTPServer server;
    public static long startTime;

    @Inject
    public static Logger logger;
    private static CommentedConfigurationNode configNode;

    private Task serverTask;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    private Path configFile;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    public static CommentedConfigurationNode getConfig() {
        return configNode;
    }

    @Inject
    private void setLogger(Logger logger) {
        ServerAPISponge.logger = logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        server = new HTTPServer(getConfig().getNode("server_api", "port").getInt(), new ServerImplSponge());

        serverTask = Task.builder().execute(server)
                .async().name("ServerAPI server")
                .submit(this);

        startTime = System.currentTimeMillis();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        serverTask.cancel();
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        configFile = configDir.resolve("config.conf");
        configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();

        configSetup();
    }

    private void configSetup() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        if (!Files.exists(configFile)) {
            try {
                //noinspection OptionalGetWithoutIsPresent
                Sponge.getAssetManager().getAsset(this, "config.conf").get().copyToFile(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        load();
    }

    private void load() {
        try {
            configNode = configLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
