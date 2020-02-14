package ru.alfomine.serverapi.api;

public class ServerInfo {
    public int publicPlayerCount;
    public int maxPlayers;
    public long serverUptime;
    public int serverTPS;

    ServerInfo(int publicPlayerCount, int maxPlayers, int serverUptime, int serverTPS) {
        this.publicPlayerCount = publicPlayerCount;
        this.maxPlayers = maxPlayers;
        this.serverUptime = serverUptime;
        this.serverTPS = serverTPS;
    }
}
