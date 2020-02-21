package ru.alfomine.serverapi.api;

import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;

public interface IServer {
    String runCommand(String command) throws ServerAPIBaseException; // Выполнить команду

    String getPlayerScreenshot(String nick) throws ServerAPIBaseException; // Получить скриншот игрока

    void sendDiscordMessage(String nick, String message) throws ServerAPIBaseException; // Отправить на сервер сообщение из дискорда

    String[] getPlayerList(boolean publicList) throws ServerAPIBaseException; // получить список игроков. Если publicList == true, то не указывать игроков в ванише.

    ServerInfo getServerInfo() throws ServerAPIBaseException; // Получить информацию о сервере для Discord

    void serverLog(String message); // Сообщение от HTTP сервера (использоавть логгер плагина)
}
