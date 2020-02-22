package ru.alfomine.serverapi.api;

public class ServerInfo {
    public int publicPlayerCount;
    public int maxPlayers;
    public long serverUptime;
    public double serverTPS;

    public ServerInfo(int publicPlayerCount, int maxPlayers, long serverUptime, double serverTPS) {
        this.publicPlayerCount = publicPlayerCount;
        this.maxPlayers = maxPlayers;
        this.serverUptime = serverUptime;
        this.serverTPS = serverTPS;
    }
}
