package ru.alfomine.serverapi.server.commands;

import ru.alfomine.serverapi.CommandResult;

import java.util.List;

public class ServerCommand {
    public String getName() {
        return "";
    }

    public CommandResult run(List<String> args) {
        return new CommandResult(200, "");
    }
}
