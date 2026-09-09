# Core Banking Sample with RabbitMQ

This project is a Spring Boot 3 / Java 21 sample for a simple core banking application. It demonstrates a modern REST API for customer and account management, transaction processing, and an event-driven integration layer using RabbitMQ.

## Overview

The application currently focuses on the core banking flow:

- Customer creation and lookup
- Account creation bound to a customer
- Credit and debit operations
- Transaction history with filtering
- Swagger UI for API documentation
- JPA persistence with H2 for local/test usage
- RabbitMQ wiring for asynchronous messaging scenarios

## Tech Stack

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring AMQP / RabbitMQ
- H2 in-memory database
- Springdoc OpenAPI 3 / Swagger UI
- Maven
- JUnit 5 + MockMvc + Mockito

## Why RabbitMQ is included

RabbitMQ is used here to show how a banking system can decouple synchronous API traffic from asynchronous background processing.

Typical banking use cases for RabbitMQ in this project style:

- publishing account/transaction events after a successful debit or credit
- sending notification events to downstream services
- decoupling the core banking API from email, audit, reporting, or external integration services
- supporting a future event-driven architecture without blocking the main API request flow

In this repository, RabbitMQ is already configured with:

- a durable queue
- a direct exchange
- a routing key
- JSON message conversion for AMQP messages

This means the project is ready to extend toward event-driven workflows without tightly coupling the REST API to downstream consumers.

## Project configuration

### Application configuration

Main runtime configuration lives in:

- `src/main/resources/application.yaml`

Key configuration values:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:coreBanking;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=LEGACY
    username: coreBanking
    password: 123456
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

This is a local in-memory database setup for development and demo usage.

### Test configuration

The test profile is configured in:

- `src/test/resources/application-test.yaml`

It uses H2 in-memory storage so controller and service tests run in isolation without affecting the main app runtime database.

## RabbitMQ setup

Start RabbitMQ locally with the management plugin:

```bash
docker run --restart=always -d --hostname my-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

Then open:

- AMQP port: `5672`
- Management UI: `http://localhost:15672`
- Default credentials: `guest / guest`

The project config uses the following exchange/queue names:

- Exchange: `exchange`
- Queue: `queue`
- Routing key: `routing-key`

These values are defined in `application.yaml` under:

```yaml
explore:
  rabbitmq:
    exchange: exchange
    queue: queue
    routing-key: routing-key
```

## Runtime and build

### Requirements

- JDK 21+
- Maven
- Docker (optional, for RabbitMQ)

### Run tests

```bash
./mvnw test
```

### Run the app

```bash
./mvnw spring-boot:run
```

## API endpoints

The project exposes endpoints for banking operations around customers, accounts, and transactions.

### Customer endpoints

- `POST /api/customers`
- `GET /api/customers/{id}`
- `GET /api/customers`

### Account endpoints

- `POST /api/accounts`
- `GET /api/accounts/{accountId}`
- `GET /api/accounts/customer/{customerId}`

### Transaction endpoints

- `POST /api/accounts/{accountId}/transactions/credit`
- `POST /api/accounts/{accountId}/transactions/debit`
- `GET /api/accounts/{accountId}/transactions`
- `GET /api/transactions/{transactionId}`

The history endpoint supports:

- `limit` for the last N records
- `fromDate` and `toDate` filters for a date range

Example:

```http
GET /api/accounts/1/transactions?limit=10&fromDate=2026-01-01&toDate=2026-12-31
```

## Domain model

The app currently models a simple banking domain:

- `Customer`
- `Account`
- `Transaction`

Business rules include:

- an account belongs to a customer
- balances are stored in the account
- credit operations increase the balance
- debit operations decrease the balance
- insufficient funds are rejected
- each debit/credit creates a ledger transaction entry

## Swagger

Swagger UI is enabled with springdoc.

Access it at:

```text
http://localhost:8080/swagger-ui.html
```

## Testing strategy

The project includes both unit tests and integration tests for the core banking flow:

- `AccountServiceTest`
- `TransactionServiceTest`
- `AccountControllerIT`
- `TransactionControllerIT`

These tests validate:

- account creation
- customer validation
- credit/debit logic
- balance updates
- transaction history retrieval
- controller behavior with MockMvc

## Notes

This sample is intentionally focused on clean, readable banking domain logic and modern Spring Boot patterns rather than a fully production-grade enterprise banking platform.

RabbitMQ is prepared as an event-driven extension point and can be expanded for:

- transaction event publishing
- audit logs
- notifications
- external system integration

## Recommended next steps

- add transfer between accounts
- add transaction status / ledger categories
- publish transaction events to RabbitMQ after successful balance updates
- add database migration tools for production (Flyway/Liquibase)
- replace in-memory H2 with PostgreSQL for a realistic environment
