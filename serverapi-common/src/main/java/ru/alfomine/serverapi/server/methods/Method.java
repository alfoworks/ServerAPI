package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.List;

public class Method {
    public String getName() {
        return "";
    }

    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        return new CommandResult().ok("");
    }
}
