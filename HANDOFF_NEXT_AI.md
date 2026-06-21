# AI Handoff - Reservation System

Last updated: 2026-06-21

## 1) Current status (verified)

- Project builds/tests are green.
- Git working tree is clean on `main`.
- Test command verified:

```zsh
./mvnw test
```

- Verified result:
  - `Tests run: 64, Failures: 0, Errors: 0, Skipped: 0`
  - `BUILD SUCCESS`

## 2) What is already implemented

### Backend CRUD

Full CRUD endpoints and service logic are implemented for:

- `Client`
- `Sala`
- `Spectacol`
- `Rezervare` (including seat update and cancel)

Update DTOs exist:

- `src/main/java/com/example/spectacol/dto/UpdateClientRequest.java`
- `src/main/java/com/example/spectacol/dto/UpdateSalaRequest.java`
- `src/main/java/com/example/spectacol/dto/UpdateSpectacolRequest.java`

### Testing

Service tests:

- `ClientServiceTest` (9)
- `SalaServiceTest` (8)
- `SpectacolServiceTest` (9)
- `RezervareServiceTest` (8)

Controller HTTP tests (WebMvc):

- `ClientControllerMvcTest` (8)
- `SalaControllerMvcTest` (7)
- `SpectacolControllerMvcTest` (7)
- `RezervareControllerMvcTest` (8)

Notes:

- Duplicate direct controller unit tests were removed (`ControllersTest`, `RezervareControllerTest`).
- Controller tests use `@WebMvcTest` + `@MockitoBean` for services.
- `GlobalExceptionHandler` is imported in controller tests to assert error JSON payloads.
- In this setup, tests use a local `ObjectMapper` in each test class (instead of autowiring one).

## 3) Important context for next AI

- Spring Boot parent version is `4.0.2` in `pom.xml`.
- Package naming still uses `com.example.spectacol` (intentionally kept for now).
- Current focus is mandatory requirements before optional microservices.

## 4) Remaining mandatory items (priority)

1. Spring Security (10%)
2. Multi-environment profiles dev/test + H2 (5%)
3. Pagination & sorting (6%)
4. Logging config + service logs (4%)
5. Better domain exceptions and error payloads
6. Integration tests with `@SpringBootTest` (7%)
7. Views/frontend + validation pages (10%)

## 5) Recommended next implementation slice

Start with **Spring Security**:

- Add security config class.
- Add JDBC auth with roles `USER` and `ADMIN`.
- Protect endpoints by role.
- Add password encoder (BCrypt).
- Add custom login/logout flow.

Then run:

```zsh
./mvnw test
```

and update this handoff file after changes.

## 6) Companion docs to read first

- `AGENTS.md`
- `README_LOCAL_PLAN.md`

