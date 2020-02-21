package ru.alfomine.serverapi.server;

import ru.alfomine.serverapi.api.ServerInfo;

public class CommandResult {
    public transient int code;
    public String error;
    public Object response;

    public CommandResult ok(String response) {
        this.code = 200;
        this.response = response;

        return this;
    }

    public CommandResult ok(String[] response) {
        this.code = 200;
        this.response = String.join(",", response);

        return this;
    }

    public CommandResult ok(ServerInfo response) {
        this.code = 200;
        this.response = response;

        return this;
    }

    public CommandResult error(String error, int code) {
        this.code = code;
        this.error = error;

        return this;
    }
}
