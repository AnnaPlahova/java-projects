# Tic-Tac-Toe (Крестики-Нолики)

Веб‑приложение на Java с JWT‑авторизацией, поддержкой игры с ботом и между игроками, историей игр и таблицей лидеров. Проект демонстрирует работу со слоями приложения (domain, datasource, web, di), интеграцией с PostgreSQL и реализацией безопасности на Spring Security.

### Использованные технологии

Язык: Java
Фреймворк: Spring Boot
Безопасность: JWT (access/refresh токены), ролевая модель 
База данных: PostgreSQL
ORM: Hibernate / JPA
Сборка: Gradle
HTTP‑клиент для тестов: curl + jq (опционально)
RESTful API

### Особенности реализации

Слои приложения: web, domain, datasource, di — разделены по пакетам.
JWT‑авторизация: access/refresh токены, валидация, ролевая модель (одна роль - USER).
История игр: выборка завершённых игр по пользователю.
Таблица лидеров: агрегация статистики (победы/поражения/ничьи), сортировка по соотношению побед.
Поддержка бота: алгоритм «Минимакс» для игры против компьютера.

### Инструкция

---

**РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ** 

В случае успешной регистрации — `User registered successfully`.

```bash
curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"login":"testuser8","password":"mypassword8"}'
```

---

**АВТОРИЗАЦИЯ ПОЛЬЗОВАТЕЛЯ** 

Возвращает токены `ACCESSTOKEN` и `REFRESHTOKEN`.

```bash
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"login":"testuser1","password":"mypassword1"}' | jq
```

---

**ПОЛУЧЕНИЕ ИНФОРМАЦИИ О ПОЛЬЗОВАТЕЛЕ ПО ACCESSTOKEN** (ввести ACCESSTOKEN)

```bash
curl -s -X GET http://localhost:8080/api/auth/me -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" | jq
```

---

**РЕФРЕШ ACCESS-ТОКЕНА** — без авторизации (ввести REFRESHTOKEN) 

Возвращает новый ACCESSTOKEN и старый REFRESHTOKEN.

```bash
curl -s -X POST http://localhost:8080/api/auth/refresh/access -H "Content-Type: application/json" -d '{"refreshToken":"REFRESHTOKEN"}' | jq
```

---

**РЕФРЕШ РЕФРЕШ-ТОКЕНА** — с авторизацией по access токену (ввести ACCESSTOKEN и REFRESHTOKEN) 

Возвращает 2 новых токена.

```bash
curl -s -X POST http://localhost:8080/api/auth/refresh/refresh -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" -d '{"refreshToken":"REFRESHTOKEN"}' | jq
```

---

**ПОЛУЧЕНИЕ ВСЕХ ЗАВЕРШЕННЫХ ИГР ПО ACCESSTOKEN ПОЛЬЗОВАТЕЛЯ** (ввести ACCESSTOKEN)

```bash
curl -s -X GET http://localhost:8080/game/history -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" | jq
```

---

**ПОЛУЧЕНИЕ ПЕРВЫХ N ЛУЧШИХ ИГРОКОВ** (ввести ACCESSTOKEN)

```bash
curl -s -X GET "http://localhost:8080/api/leaderboard/top/3" -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" | jq
```

---

**СТАРТ ИГРЫ** (с авторизацией): выбрать `vsBot=true` или `vsBot=false`, ввести ACCESSTOKEN 

Возвращает ID игры, статус и пустое игровое поле.

```bash
curl -s -X POST "http://localhost:8080/game/start?vsBot=true" -H "Authorization: Bearer ACCESSTOKEN"
```

---

**ПРОЦЕСС ИГРЫ:** ввести ID игры, ACCESSTOKEN игрока, который ходит, поле с 1 ходом (x=1, 0=2) 

Возвращает поле, статус игры (ходит X/0 — выиграл X/0 — ничья).

```bash
curl -s -X POST "http://localhost:8080/game/.../move" -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN" -d '{"field":[[0,0,0],[0,1,0],[0,0,0]]}'
```

---

**ПРИСОЕДИНИТЬСЯ К ИГРЕ:** авторизация пользователя, затем ввести ID игры, ACCESSTOKEN игрока

```bash
curl -s -X POST "http://localhost:8080/game/.../join" -H "Content-Type: application/json" -H "Authorization: Bearer ACCESSTOKEN"
```

---
