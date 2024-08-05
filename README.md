# Bank RESTful API

Этот проект представляет собой RESTful API для управления банковскими счетами и транзакциями. Приложение разработано с использованием Spring Boot и предоставляет CRUD операции для управления счетами, бенефициарами и транзакциями.

## Установка и запуск

### Требования

- Java 11 или выше
- Maven 3.6.0 или выше

### Сборка и запуск приложения

1. Клонируйте репозиторий:
    ```sh
    git clone <URL>
    cd <>
    ```

2. Соберите проект с помощью Maven:
    ```sh
    mvn clean install
    ```

3. Запустите приложение:
    ```sh
    mvn spring-boot:run
    ```

Приложение будет доступно по адресу `???????`.

## Использование

### Конечные точки API

#### Управление аккаунтами

- **Получить все счета**
    ```http
    GET /api/accounts
    ```

- **Создать счет**
    ```http
    POST /api/accounts/create
    ```

#### Управление транзакциями

- **Получить транзакции по номеру счета**
    ```http
    GET /api/transactions/{accountNumber}
    ```

- **Внести средства**
    ```http
    POST /api/transactions/deposit
    ```

- **Снять средства**
    ```http
    POST /api/transactions/withdraw
    ```

- **Перевести средства**
    ```http
    POST /api/transactions/transfer
    ```

### Примеры запросов

#### Создание счета

```http
POST /api/accounts/create
Content-Type: application/json

{
  "name": "John Doe",
  "pinCode": "1234"
}
```

#### Внесение средств

```http 
POST /api/transactions/deposit
Content-Type: application/json

{
  "accountNumber": 1,
  "amount": 100
}
```

### Снятие средств
```http
POST /api/transactions/withdraw
Content-Type: application/json

{
  "accountNumber": 1234567890,
  "amount": 50,
  "pinCode": "1234"
}
```
### Перевод средств
```http
POST /api/transactions/transfer
Content-Type: application/json

{
  "fromAccountNumber": 1234567890,
  "toAccountNumber": 0987654321,
  "amount": 25,
  "pinCode": "1234"
}
```

Приложение обрабатывает следующие исключения и возвращает соответствующие HTTP статусы:

MethodArgumentNotValidException: 400 Bad Request
AccountNotFoundException: 404 Not Found
InsufficientFundsException: 400 Bad Request
InvalidPinException: 401 Unauthorized
Exception: 500 Internal Server Error


