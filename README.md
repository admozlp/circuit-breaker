# Microservice Resilience Patterns

A Spring Boot demo showcasing **Circuit Breaker** and **Retry** resilience patterns using Resilience4j and OpenFeign.

## Patterns Implemented

| Pattern | Library | Description |
|---|---|---|
| Circuit Breaker | Resilience4j | Opens after 50% failure rate, waits 10s before retrying |
| Retry | Resilience4j | Retries up to 3 times with 1s delay on retryable errors |
| Feign Client | Spring Cloud OpenFeign | Declarative HTTP client for downstream service calls |
| Error Decoder | Feign | Maps HTTP 5xx / 429 responses to retryable exceptions |

## How It Works

```
POST /users  →  UserService  →  CityService  →  CityClient  →  localhost:4040/cities
```

1. `CityClient` calls an external city service at `localhost:4040/cities`
2. On `5xx` or `429` responses, `CityClientErrorDecoder` throws a `RetryableException`
3. Resilience4j **Retry** retries the call up to 3 times
4. **Circuit Breaker** tracks failure rate and short-circuits if it exceeds 50%
5. After all retries are exhausted, a fallback method throws a `CityException`
6. `GlobalExceptionHandler` handles it gracefully

## Requirements

- Java 21+
- Gradle
- A running city service at `localhost:4040`

## Running

```bash
./gradlew bootRun
```

## Endpoint

```
POST http://localhost:8080/users
Content-Type: text/plain

Body: <username>
```

## Tech Stack

- Spring Boot 3.4
- Resilience4j 2.3
- Spring Cloud OpenFeign
- Lombok
