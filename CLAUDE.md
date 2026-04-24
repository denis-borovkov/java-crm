# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=AuthControllerTest

# Run a single test method
./mvnw test -Dtest=AuthControllerTest#signupCreatesUser
```

## Architecture

Spring Boot 4.0 / Java 21 REST backend for a CRM. MySQL database with JPA/Hibernate (`ddl-auto: update`). MapStruct for entity-to-DTO mapping. Lombok for boilerplate reduction.

**Package layout:** `com.denisborovkov.javacrm`
- `controller` — REST endpoints
- `service` — business logic
- `repository` — Spring Data JPA interfaces
- `entity` — JPA entities
- `dto` — request/response records
- `mapper` — MapStruct interfaces
- `config` — Spring Security, JPA auditing
- `enums` — `Role` (USER, ADMIN), `CompanyRole`, `Status`
- `exception` — custom exceptions
- `abstraction` — `BaseEntity` with JPA auditing fields (`createdAt`, `updatedAt`, `createdBy`, `updatedBy`)

**Auth flow (stateless JWT):**
- `JwtService` issues access tokens (15 min) and refresh tokens (7 days), signed with `spring.application.key`
- `TokenService` manages refresh token lifecycle in the DB — rotation on refresh, revocation cascade on reuse detection
- `JpaOneTimeTokenService` handles password-reset one-time tokens with configurable cooldown (`app.security.recovery.cooldown`, default 1 min) and TTL (`app.security.recovery.ttl`, default 15 min)
- `JwtAuthFilter` validates the `Authorization: Bearer` header on every request
- Public endpoints: `/api/v1/auth/signin`, `/api/v1/auth/signup`, `/api/v1/auth/refresh`, `/api/v1/auth/forgot`, `/api/v1/auth/reset`, `/api/v1/auth/logout`
- Admin-only: `/api/v1/admin/**` (checked via `hasRole('ADMIN')`)

**Controllers and their base paths:**
- `AuthController` — `/api/v1/auth`
- `UserController` — `/api/v1/users`
- `AdminController` — `/api/v1/admin`
- `CustomerController` — `/customers` (note: no `/api/v1` prefix)

## Configuration

The database is `java_crm` on `localhost:3306`.
