package ru.alfomine.serverapi.sponge;

import org.spongepowered.api.Sponge;

public class CommandRunTask implements Runnable {
    public static String lastCmd;
    public static boolean end = false;

    private String command;

    public CommandRunTask(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        ServerAPICommandSource source = new ServerAPICommandSource();

        Sponge.getCommandManager().process(source, command);

        lastCmd = source.getOutput();
        end = true;
    }
}
