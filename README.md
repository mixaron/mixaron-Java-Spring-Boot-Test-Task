## Package: ru.ryabchuk.testtask.controller.auth

Этот пакет содержит контроллеры, связанные с аутентификацией и авторизацией пользователей.

В файле `application-secret.properties`  необходимо указать следующие свойства(представлен properties для postgresql):

```properties
db.username=your_username
db.password=your_password
secret.key=your_secret_key
```
### AuthController

Контроллер `AuthController` предоставляет следующие HTTP-методы:

- `POST /api/auth/register`: Регистрация нового пользователя.
 ```
{
    "name": "Anton",
    "age": 21,
    "password": "securepassword"
}
```
- `POST /api/auth/authenticate`: Аутентификация пользователя и получение JWT-токена.
```
 {
    "name": "Anton",
    "password": "securepassword"
}
```
### AuthenticationRequest

Класс `AuthenticationRequest` представляет собой DTO (Data Transfer Object), который используется для передачи учетных данных пользователя при аутентификации. Он содержит поля `name` и `password`.

## Package: ru.ryabchuk.testtask.config

Этот пакет содержит конфигурацию безопасности приложения.

### SecurityConfig

Класс `SecurityConfig` настраивает безопасность приложения, определяя, какие URL-адреса должны быть защищены и какие нет. Он также настраивает `AuthenticationProvider` и добавляет фильтр `JWTAuthenticationFilter` перед фильтром `UsernamePasswordAuthenticationFilter` для обработки аутентификации JWT-токенов.

Конфигурация включает в себя следующее:

- Отключение CSRF-защиты для упрощения тестирования.
- Настройка авторизации, разрешая доступ к URL-адресам, соответствующим шаблону `/api/auth/**` без аутентификации.
- Настройка политики создания сессий, установленной в `STATELESS`, что означает, что сессии не будут использоваться для хранения аутентификации пользователя.
- Добавление `AuthenticationProvider` для обработки аутентификации.
- Добавление фильтра `JWTAuthenticationFilter` перед фильтром `UsernamePasswordAuthenticationFilter` для обработки аутентификации JWT-токенов.

Также закомментирован метод `userDetailsService()`, который предназначен для настройки `InMemoryUserDetailsManager` с пользователем по умолчанию. Это может быть полезно для тестирования или при небольшом количестве пользователей, но в более сложных сценариях обычно используются более сложные механизмы аутентификации, такие как база данных или LDAP.
# Package: ru.ryabchuk.testtask.controller

Этот пакет содержит контроллеры для обработки HTTP-запросов, связанных с сущностями `Person`, `House` и `HouseResident`. Каждый контроллер отвечает за определенный набор операций, таких как получение, обновление, создание и удаление сущностей.

## PersonController

Контроллер `PersonController` предоставляет следующие HTTP-методы:

- `GET /api/person`: Получение списка всех персон.
- `GET /api/person/{id}`: Получение персоны по идентификатору.
- `PUT /api/person`: Обновление персоны.
- `DELETE /api/person`: Удаление персоны.

## ResidentController

Контроллер `ResidentController` предоставляет следующие HTTP-методы:

- `GET /api/residents`: Получение списка жильцов текущего пользователя.
- `GET /api/residents/{id}`: Получение списка жильцов по идентификатору дома.
- `POST /api/residents/{id}`: Установка жильца в дом.
- `DELETE /api/residents/{id}`: Исключение жильца из дома.

## HouseController

Контроллер `HouseController` предоставляет следующие HTTP-методы:

- `GET /api/house`: Получение списка всех домов.
- `GET /api/house/{id}`: Получение дома по идентификатору.
- `POST /api/house`: Создание нового дома.
```
{
    "address": "123"
}
```
- `PUT /api/house/{id}`: Обновление дома по идентификатору.
- `DELETE /api/house/{id}`: Удаление дома по идентификатору.
- `POST /api/house/{id}`: Установка нового владельца дома.

Каждый контроллер использует соответствующий сервис для выполнения операций с базой данных. Обратите внимание, что методы `updateUser` и `deletePerson` в `PersonController` и `deletePerson` в `HouseController` имеют неопределенное поведение, так как они не принимают идентификатор сущности для обновления или удаления.
