## Описание

Веб-приложение предоставляет функционал генерации коротких ссылок.
Реализован frontend и backend (выполнены как отдельные проекты).
Реализована авторизация пользователей с помощью **JWT**.

### Основные возможности
1. Создание коротой ссылки по полному URL. Короткая ссылка состоит из 8 символов диапазона: [0-9, a-z].
2. По известной короткой ссылке осуществляется перенаправление браузера пользователя на исходный URL.
3. Сервис доступен только после регистрации. Пользователь может управлять своими ссылками.
4. Предусмотрена возможность удаления зарегистрированных коротких ссылок.
5. Предусмотрена возможность установки ограничения на срок “жизни” короткой ссылки (один раз в сутки происходит удаление просроченных ссылок).
6. Выполняется подсчет количества переходов по ссылке (в таблице со ссылками столбец Follows).
7. Выполняется подсчет уникальных переходов по ссылке, (в таблице со ссылками столбец Unique follows).
8. Данные в столбцах Follows и Unique follows обновляются в режиме реального времени за счет использования протокола **WebSocket**.

### Используемые технологии:

- Spring Boot

- Vue

- JWT authorization

- Liquibase

- WebSocket

### endpoints

- `POST /links` - генерация ссылки. Поле *originUrl* является обязательным. Поле *dateOfExpiration* опциональное. Если данное поле отсутствует, ссылка будет бесконечной.

  Пример тела запроса:
  
  ```json
    {
        "originUrl": "https://some_url.com",
        "dateOfExpiration": "05.05.2022"
    }
  ```

- `GET /links` - получение списка ссылок зарегистрированным пользователем. 

- `GET /{hash}` - Редирект короткой ссылки на origin URL.

- `DELETE /links` - Удаление ссылки. Параметр - *hash*

- `POST /register` - регистрация пользователя. Обязательные параметры: *email*, *firstName*, *password*. Параметр *lastName* опциональный. Если не заполнен один из обязательных параметров возвращается status code **400 BAD_REQUEST**. В случае успеха вернется status code **201 CREATED**. 

  Пример тела запроса:
  
  ```json
  {
    "email": "email@mail.ru",
    "firstName": "Alex",
    "password": "password",
    "lastName": "Smith"
  }
  ```

- `POST /auth` - получение **JWT** зарегистрированным пользователем. 

  Пример тела запроса:
  
  ```json
  {
    "email": "email@mail.ru",
    "password": "password"
  }
  ```
  В случае отправки запроса на данный endpoint с не валидными данными также вернется 
  status code **400 BAD_REQUEST**.

## Сборка и запуск проекта

Для работы приложения требуется **JDK** версии 8 или выше.

Сейчас в проекте указана версия JDK 11. Если будет использоваться более низкая версия, 
нужно указать ее в параметре `java.version` в файле `pom.xml`.

Для работы **frontend** требуется **Node.js** и **npm**.

Перед запуском приложения необходимо создать базу данных **Postgres** с именем 
**shlink**, пользователем **postgres** и паролем **postgres**.

### Запуск backend
- Установить самоподписанный сертификат, расположенный в директории `/resources/keystore`.
  
  - В Windows, открыть двойным щелчком мыши файл *websock-local.crt* и установить сертификат.
  
  - В Linux, находясь в директории `keystore`, выполнить команды:
  
    ```
    sudo mkdir /usr/local/share/ca-certificates/extra
    sudo cp websock-local.crt /usr/local/share/ca-certificates/extra/websock-local.crt
    sudo update-ca-certificates
    ```

    Для работы приложения в Chrome под Linux нужно установить флаг 
    *Allow invalid certificates for resources loaded from localhost*.
    Для этого в браузере нужно перейти по адресу: `chrome://flags/#allow-insecure-localhost`,
    выбрать *Enabled* и перезапустить браузер. 
   
- Клонировать проект

  `git clone https://github.com/ignal1/shlink.git`

- Перейти в директорию *backend* и в терминале выполнить команду

  `./mvnw spring-boot:run` (для Linux)
  
  или
  
  `mvnw spring-boot:run` (для Windows)

### Запуск frontend

В новом окне терминала перейти в директорию *frontend* и  выполнить команды

```
npm install
npm run serve
```

После этого приложение будет доступно на *https://127.0.0.1:8081*.

## Завершение работы приложения

Для завершения процессов, в каждом окне терминала

- нажать `Ctrl + Z` (для Linux)
   
- нажать `Ctrl + C`, после чего ввести в терминале `y` и нажать `Enter` (для Windows)

По окончании работы с приложением в Chrome под Windows нужно удалить 
настройки *HSTS* для *localhost*, чтобы  в дальнейшем можно было 
работать с незащищенным *localhost* в Chrome.
Для этого можно открыть панель разработчика (F12), щелкнуть правой 
кнопком мыши на кнопке *Обновление страницы* и в меню выбрать 
*Очистка кеша и жесткая перезагрузка*.

