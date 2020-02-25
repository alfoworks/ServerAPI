package ru.alfomine.serverapi.sponge;

import org.spongepowered.api.Sponge;

public class CommandRunTask implements Runnable {
    String output = null;
    private String command;
    private String senderName;

    CommandRunTask(String command, String senderName) {
        this.command = command;
        this.senderName = senderName;
    }

    @Override
    public void run() {
        ServerAPICommandSource source = new ServerAPICommandSource(senderName);

        Sponge.getCommandManager().process(source, command);

        if (source.getOutput().equals("")) {
            try {
                Thread.sleep(500); // Зачем? Асинхронные команды (которые очень нужны) просто не успеют ничего высрать.
            } catch (InterruptedException ignored) {

            }
        }

        output = source.getOutput();
    }
}
