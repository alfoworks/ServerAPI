package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.List;

public class MethodInfo extends Method {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        return new CommandResult().ok(server.getServerInfo());
    }
}
