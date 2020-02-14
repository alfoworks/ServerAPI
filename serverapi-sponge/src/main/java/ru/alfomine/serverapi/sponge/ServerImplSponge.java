package ru.alfomine.serverapi.sponge;

import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.ServerInfo;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

public class ServerImplSponge implements IServer {
    @Override
    public void runCommand(String command) throws ServerAPIBaseException {

    }

    @Override
    public byte[] getPlayerScreenshot(String nick) throws ServerAPIBaseException {
        return new byte[0];
    }

    @Override
    public void sendDiscordMessage(String nick, String message) throws ServerAPIBaseException {

    }

    @Override
    public String[] getPlayerList(boolean publicList) throws ServerAPIBaseException {
        return new String[0];
    }

    @Override
    public ServerInfo getServerInfo() throws ServerAPIBaseException {
        return null;
    }

    @Override
    public void serverLog(String message) {

    }
}
