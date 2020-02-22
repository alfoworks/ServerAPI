package ru.alfomine.serverapi.server.methods;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.*;
import ru.alfomine.serverapi.server.CommandResult;

import java.util.Arrays;
import java.util.List;

public class MethodScreenshot extends Method {
    private List<String> allowedModes = Arrays.asList("low", "high", "grayscale");

    @Override
    public String getName() {
        return "scr";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        if (args.size() < 1) {
            return new CommandResult().error("Not enough arguments!", 400);
        }

        String quality = "low";

        if (args.size() > 1 && !allowedModes.contains(args.get(1))) {
            return new CommandResult().error("Wrong quality", 400);
        } else if (args.size() > 1) {
            quality = args.get(1);
        }

        String screenshot;

        try {
            screenshot = server.getPlayerScreenshot(args.get(0), quality);
        } catch (PlayerNotFoundException e) {
            return new CommandResult().error(e.getMessage(), 404);
        } catch (ScreenshotTimeoutException e) {
            return new CommandResult().error(e.getMessage(), 524);
        } catch (ScreenshotNotSupportedException e) {
            return new CommandResult().error(e.getMessage(), 405);
        } catch (ScreenshotException e) {
            return new CommandResult().error(e.getMessage(), 500);
        }

        return new CommandResult().ok(screenshot);
    }
}
