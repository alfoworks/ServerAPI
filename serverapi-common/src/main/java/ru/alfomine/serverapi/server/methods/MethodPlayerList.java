package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.List;

public class MethodPlayerList extends Method {
    @Override
    public String getName() {
        return "players";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        boolean publicList = false;

        if (args.size() > 0) {
            publicList = args.get(0).equalsIgnoreCase("true");
        }

        return new CommandResult().ok(server.getPlayerList(publicList));
    }
}
