package ru.alfomine.serverapi.api.exception;

public class PlayerNotFoundException extends ServerAPIBaseException {
    public PlayerNotFoundException() {
        super("Player wasn't found.");
    }
}
