# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Compile and run tests
mvn clean test

# Package (skip tests)
mvn clean package -DskipTests

# Run the app
mvn spring-boot:run
```

The main class is `CampusTradeApplication`. Before running, create a MySQL database named `second_hand` and update credentials in `application.yml` if needed.

## Tech Stack

- **Backend**: Spring Boot 4.0.6 (Java 17), Spring MVC, MyBatis-Plus 3.5.16, Druid connection pool, Jakarta Validation
- **Database**: MySQL (`second_hand`)
- **Frontend**: Vue 3 + Axios via CDN, pure CSS — no bundler, no npm. Served as static HTML files from `frontend/`.

## Architecture

Standard layered architecture: **Controller → Service → Mapper (MyBatis-Plus BaseMapper)**.

```
controller/     REST endpoints, parameter validation (@Valid/@Validated), return R<T>
service/        Business logic, uses MyBatis-Plus QueryWrapper/LambdaQueryWrapper
mapper/         Interfaces extending BaseMapper<T> — no custom SQL needed
entity/         Entity classes mapped to DB tables, plus R.java (unified response)
dto/            Request DTOs for specific endpoints (LoginRequest, UpdateUserRequest)
config/         CORS, MyBatis-Plus pagination plugin, interceptor registration
interceptor/    LoginInterceptor — reads userId from HttpSession
handler/        MyMetaObjectHandler — auto-fills createTime/updateTime
exception/      BusinessException + GlobalExceptionHandler (9 exception types → R JSON)
```

## Key Patterns

### Unified Response — R&lt;T&gt;

Every endpoint returns `R<T>`, which serializes to `{"code": 200, "message": "success", "data": ...}`. Use static factories:
- `R.ok(data)` / `R.ok()` — code 200
- `R.error(message)` — code 500
- `R.error(code, message)` — custom code

The `GlobalExceptionHandler` catches all exceptions and returns `R` with appropriate HTTP status codes, so controllers never need try-catch for error responses.

### Authentication (Session-based, not JWT)

Login stores `userId` and `username` in `HttpSession`. `LoginInterceptor` extracts `userId` from the session on every request and sets it as a request attribute (`request.getAttribute("userId")`). The interceptor **always passes through** (`return true`) — individual controller methods check for a non-null userId attribute to enforce auth. Protected endpoints: `/users/me`, `/products/release`, `/products/my_release`, `PUT /users/{id}`.

Password comparison is plain-text (no hashing yet).

### Validation Pattern

- Request body objects use Jakarta annotations (`@NotBlank`, `@NotNull`, etc.) with `@Valid` on the controller parameter.
- Pagination params use `@Min`/`@Max` on method parameters, requiring `@Validated` on the controller class.
- DTOs are used when entity validation rules differ across endpoints (e.g., `LoginRequest` for login vs `UpdateUserRequest` for profile updates).
- Validation failures are caught by `GlobalExceptionHandler` and return `R.error(400, "具体提示")` — no manual `if` checks needed in controllers.

### MyBatis-Plus Conventions

- Logical delete: `deleted` field (0/1), configured globally in `application.yml`
- Auto-fill: `createTime` and `updateTime` set by `MyMetaObjectHandler`
- Pagination: `MybatisPlusConfig` registers a `PaginationInnerInterceptor` for MySQL
- All mappers extend `BaseMapper<T>` — CRUD is inherited, custom queries use `QueryWrapper`

### Frontend

All API calls go to `http://localhost:8080` (hardcoded `BASE_URL` per page). `axios.defaults.withCredentials = true` is set on pages that need session cookies. The CORS config allows origin `http://localhost:63342` (IntelliJ/WebStorm live preview port) with credentials.

### Static Resource Handling

`application.yml` disables default static resource mapping (`add-mappings: false`) and throws on no-handler-found. The `resources/static/` and `resources/templates/` directories are empty — the frontend is served independently.

### Configuration Notes

`application.yml` sets `throw-exception-if-no-handler-found: true` alongside `static-path-pattern` and `static-locations` under web resources. This combination means the static resource config in the YAML is inert (disabled by `add-mappings: false`), and missing endpoints correctly produce 404 responses via `NoHandlerFoundException`.
