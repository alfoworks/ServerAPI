package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.List;

public class MethodCommand extends Method {
    @Override
    public String getName() {
        return "command";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        if (args.size() < 1) {
            return new CommandResult().error("Not enough arguments!", 400);
        }

        String senderName = "ServerAPI";
        String group = null;

        if (args.size() > 1) {
            senderName = String.format("@%s", args.get(1)).substring(0, Math.min(args.get(1).length() + 1, 16));

            if (args.size() > 2) {
                group = args.get(2);
            }
        }

        return new CommandResult().ok(server.runCommand(args.get(0), senderName, group));
    }
}
