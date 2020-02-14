package ru.alfomine.serverapi.api.exception;

public class ServerAPIBaseException extends Exception {
    public ServerAPIBaseException(String errorMessage) {
        super(errorMessage);
    }
}
