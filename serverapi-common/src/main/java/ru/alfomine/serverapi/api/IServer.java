package ru.alfomine.serverapi.api;

import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

public interface IServer {
    public void runCommand(String command) throws ServerAPIBaseException; // Выполнить команду

    public byte[] getPlayerScreenshot(String nick) throws ServerAPIBaseException; // Получить скриншот игрока

    public void sendDiscordMessage(String nick, String message) throws ServerAPIBaseException; // Отправить на сервер сообщение из дискорда

    public String[] getPlayerList(boolean publicList) throws ServerAPIBaseException; // получить список игроков. Если publicList == false, то не указывать игроков в ванише.

    public ServerInfo getServerInfo() throws ServerAPIBaseException; // Получить информацию о сервере для Discord

    public void serverLog(String message); // Сообщение от HTTP сервера (использоавть логгер плагина)
}
