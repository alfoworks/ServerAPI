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

        if (output.equals("")) {
            try {
                Thread.sleep(1000); // Зачем? Асинхронные команды (которые очень нужны) просто не успеют ничего высрать.
            } catch (InterruptedException ignored) {

            }
        }

        output = source.getOutput();
    }
}
