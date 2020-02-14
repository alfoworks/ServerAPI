package ru.alfomine.serverapi.api.exception;

public class ScreenshotTimeoutException extends ServerAPIBaseException {
    public ScreenshotTimeoutException() {
        super("Screenshot timeout reached.");
    }
}
