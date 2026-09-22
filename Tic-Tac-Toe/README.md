# Tic-Tac-Toe (Крестики-Нолики)

Веб‑приложение на Java с JWT‑авторизацией, поддержкой игры с ботом и между игроками, историей игр и таблицей лидеров. Проект демонстрирует работу со слоями приложения (domain, datasource, web, di), интеграцией с PostgreSQL и реализацией безопасности на Spring Security.

## Использованные технологии

- Язык: Java
- Фреймворк: Spring Boot
- Безопасность: JWT (access/refresh токены), ролевая модель 
- База данных: PostgreSQL
- ORM: Hibernate / JPA
- Сборка: Gradle
- HTTP‑клиент: curl
- RESTful API

## Особенности реализации

- Слои приложения: web, domain, datasource, di — разделены по пакетам.
- JWT‑авторизация: access/refresh токены, валидация, ролевая модель (одна роль - USER).
- История игр: выборка завершённых игр по пользователю.
- Таблица лидеров: агрегация статистики (победы/поражения/ничьи), сортировка по соотношению побед.
- Поддержка бота: алгоритм «Минимакс» для игры против компьютера.

## Запуск проекта

```bash
./gradlew clean bootRun
```

## Инструкция

Если jq не установлен, используйте команды без | jq


### Регистрация пользователя

В случае успешной регистрации вернется `User registered successfully`.

```bash
curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"login":"testuser8","password":"mypassword8"}'
```

### Авторизация пользователя 

Возвращает токены `ACCESSTOKEN` и `REFRESHTOKEN`.

```bash
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"login":"testuser1","password":"mypassword1"}' | jq
```

### Получение информации о пользователе по access-токену

Требуется действующий `ACCESSTOKEN`.

```bash
curl -s -X GET http://localhost:8080/api/auth/me -H "Authorization: Bearer ACCESSTOKEN" | jq
```

### Рефреш access-токена

Без авторизации. Требуется `REFRESHTOKEN`. Возвращает новый `ACCESSTOKEN` и старый `REFRESHTOKEN`.

```bash
curl -s -X POST http://localhost:8080/api/auth/refresh/access -H "Content-Type: application/json" -d '{"refreshToken":"REFRESHTOKEN"}' | jq
```

### Рефреш refresh-токена 

С авторизацией по `ACCESSTOKEN`. Требуется `ACCESSTOKEN` и `REFRESHTOKEN`. Возвращает 2 новых токена.

```bash
curl -s -X POST http://localhost:8080/api/auth/refresh/refresh -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" -d '{"refreshToken":"REFRESHTOKEN"}' | jq
```

### Получение истории игр по access-токену пользователя

Требуется авторизация по `ACCESSTOKEN`. Возвращает список завершённых игр пользователя.

```bash
curl -s -X GET http://localhost:8080/game/history -H "Authorization: Bearer ACCESSTOKEN" | jq
```

### Получение лучших игроков 

Параметр `{limit}` задаёт количество игроков в топе. Требуется авторизация по `ACCESSTOKEN`. Возвращает топ игроков.

```bash
curl -s -X GET "http://localhost:8080/api/leaderboard/top/{limit}" -H "Authorization: Bearer ACCESSTOKEN" | jq
```

### Старт игры

Выберите игру против бота (`vsBot=true`) или против другого игрока (`vsBot=false`). Требуется авторизация по `ACCESSTOKEN`. Возвращает ID игры, статус и пустое поле.

```bash
curl -s -X POST "http://localhost:8080/game/start?vsBot=true" -H "Authorization: Bearer ACCESSTOKEN"
```

### Ход в игре

Требуется `{gameId}` и `ACCESSTOKEN` игрока, который ходит. В теле запроса передаётся поле `field` с 1 ходом (ход X = 1, ход O = 2).

Возвращает поле с ходом игрока/игрока и бота, статус игры (ходит X/O — выиграл X/O — ничья).

```bash
curl -s -X POST "http://localhost:8080/game/{gameId}/move" -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" -d '{"field":[[0,0,0],[0,1,0],[0,0,0]]}'
```

### Присоединиться к игре

Требуется `{gameId}` и авторизация по `ACCESSTOKEN` пользователя.

```bash
curl -s -X POST "http://localhost:8080/game/{gameId}/join" -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN"
```

### Получить состояние игры по ID

Требуется `{gameId}` и авторизация по `ACCESSTOKEN`.

```bash
curl -s -X GET "http://localhost:8080/game/{gameId}" -H "Authorization: Bearer ACCESSTOKEN" | jq
```
