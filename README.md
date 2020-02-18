#ServerAPI (json)

### Документация:
В каждом запросе в JSON должно быть как минимум два ключа:
* method - метод (см. ниже)
* args - аргументы к методу, если нужно. Если нет, оставлять пустым.

Структура ответа:
* response - ответ метода
* error - текст ошибки (если status code != 200)


Список методов:
* любой метод
    * 500 <текст ошибки> - при выполнении произошла ошибка
* command <команда (str)> - выполнить команду. Возвращаемый результат:
    * 200 <ответ от команды> (может быть пустым)
* players <public (bool)> - получить список игроков. При public = true, игроки в ванише не будут появлятся в списке.
    * 200 <список игроков (str array)>
* info - получить инофрмацию о сервере
    * 200 <JSON объект> (пример объедка: `{"playerCount": 228, "maxPlayers": 1488, "serverUptime": 99999999, "tps": 20}`)
* scr <ник> - получить скриншот игрока
    * 200 <base64 png>
    * 524 (превышено время ожидания ответа)
    * 400 (игрок не найден)
    * 451 (скриншот не поддерживается на этом сервере)
* discord <ник> <сообщение> - пересылка сообщения из дискорда на сервер
    * 200