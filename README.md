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

- **Получить счета по ID бенефициара**
    ```http
    GET /api/accounts/beneficiary/{beneficiaryId}
    ```

- **Создать счет по имени**
    ```http
    POST /api/accounts/create/by-name
    ```

- **Создать счет по ID**
    ```http
    POST /api/accounts/create/by-id
    ```

- **Удалить счет по ID**
    ```http
    DELETE /api/accounts/{accountId}
    ```

- **Удалить счета по ID бенефициара**
    ```http
    DELETE /api/accounts/beneficiary/{beneficiaryId}
    ```

#### Управление транзакциями

- **Получить транзакции по ID счета**
    ```http
    GET /api/transactions/{accountId}
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

#### Создание счета по имени

```http
POST /api/accounts/create/by-name
Content-Type: application/json

{
  "name": "John Doe",
  "pinCode": "1234"
}
```
#### Создание счета по ID бенефициара

```http 
POST /api/accounts/create/by-id
Content-Type: application/json

{
  "beneficiaryId": 1,
  "pinCode": "1234"
}
```
#### Внесение средств

```http 
POST /api/transactions/deposit
Content-Type: application/json

{
  "accountId": 1,
  "amount": 100
}
```

### Снятие средств
```http
POST /api/transactions/withdraw
Content-Type: application/json

{
  "accountId": 1,
  "amount": 50,
  "pinCode": "1234"
}
```
### Перевод средств
```http
POST /api/transactions/transfer
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountId": 2,
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


