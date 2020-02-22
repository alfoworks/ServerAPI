package ru.alfomine.serverapi.api.exception;

public class ScreenshotNotSupportedException extends ServerAPIBaseException {
    public ScreenshotNotSupportedException() {
        super("Screenshoting is not supported on this server.");
    }
}
