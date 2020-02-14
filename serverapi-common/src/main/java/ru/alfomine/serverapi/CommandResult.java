package ru.alfomine.serverapi;

public class CommandResult {
    private int code;
    private String body;

    public CommandResult(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
