package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.List;

public class MethodDiscord extends Method {
    @Override
    public String getName() {
        return "discord";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        if (args.size() < 2) {
            return new CommandResult().error("Not enough arguments!", 400);
        }

        String nick = args.get(0);
        String message = args.get(1);

        server.sendDiscordMessage(nick, message);

        return new CommandResult().ok("");
    }
}
