package ru.alfomine.serverapi.sponge;

import ru.alfomine.serverapi.server.HTTPServer;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "serverapi",
        name = "ServerAPI"
)
public class ServerAPISponge {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        HTTPServer server = new HTTPServer(1000, new ServerImplSponge());
    }
}
