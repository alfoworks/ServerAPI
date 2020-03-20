package ru.alfomine.serverapi.spigot;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandRunTask extends BukkitRunnable {
    String output = null;
    private String command;
    private String senderName;

    CommandRunTask(String command, String senderName) {
        this.command = command;
        this.senderName = senderName;
    }

    @Override
    public void run() {
        ServerAPICommandSender sender = new ServerAPICommandSender(senderName);

        Bukkit.getServer().dispatchCommand(sender, command);

        if (sender.getOutput().equals("")) {
            try {
                Thread.sleep(500); // Зачем? Асинхронные команды (которые очень нужны) просто не успеют ничего высрать.
            } catch (InterruptedException ignored) {

            }
        }

        output = sender.getOutput();
    }
}
