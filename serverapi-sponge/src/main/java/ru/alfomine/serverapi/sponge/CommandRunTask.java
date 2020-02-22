package ru.alfomine.serverapi.sponge;

import org.spongepowered.api.Sponge;

public class CommandRunTask implements Runnable {
    String output = null;
    private String command;

    public CommandRunTask(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        ServerAPICommandSource source = new ServerAPICommandSource();

        Sponge.getCommandManager().process(source, command);

        output = source.getOutput();

        System.out.println("Anus");
        System.out.println(output);
    }
}
