package ru.alfomine.serverapi.spigot;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ServerAPICommandSender implements CommandSender {
    String name;
    ArrayList<String> out = new ArrayList<>();

    public ServerAPICommandSender(String name) {
        this.name = name;
    }

    @Override
    public void sendMessage(String message) {
        out.add(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        out.addAll(new ArrayList<>(Arrays.asList(messages)));
    }

    public String getOutput() {
        return String.join("\n", out);
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Spigot spigot() {
        return new Spigot();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return true;
    }

    @Override
    public boolean hasPermission(String name) {
        return true;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {

    }
}
