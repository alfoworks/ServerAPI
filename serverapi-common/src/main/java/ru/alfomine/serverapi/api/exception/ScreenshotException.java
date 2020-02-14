package ru.alfomine.serverapi.api.exception;

public class ScreenshotException extends ServerAPIBaseException {
    public ScreenshotException() {
        super("Failed to take a screenshot.");
    }
}
