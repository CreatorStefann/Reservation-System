# LOCAL Project Roadmap (Do Not Commit)

> Purpose: living project notebook for planning, status tracking, commit strategy, and implementation notes.
>
> Keep this file local only.

## 0) Snapshot (2026-06-17)

- Project type now: Spring Boot monolith (REST API only)
- Domain: Theater reservation system (`spectacol` naming still present in package names)
- Build tool: Maven Wrapper (`mvnw`)
- Java version in `pom.xml`: 17
- DB configured: PostgreSQL only (`application.properties`)
- API docs: Springdoc OpenAPI UI dependency present
- UI views: none implemented yet (empty `templates/` and `static/`)
- Security: none implemented yet
- Optional microservices: not started

### Verified build/test state

Command executed:

```zsh
./mvnw -q test
```

Result:

- Test compilation fails in `src/test/java/com/example/spectacol/controller/RezervareControllerTest.java`
- Invalid import package used for `WebMvcTest`

Implication: baseline is not green; first priority is stabilization.

---

## 1) What exists right now

## Architecture and layers

- Main app class: `ReservationSystemApplication`
- Layers present:
  - `controller/` (REST endpoints)
  - `service/` (business logic)
  - `repository/` (Spring Data JPA)
  - `model/` (JPA entities)
  - `dto/` (create requests)
  - `exception/` (basic global handler)
- Tests present:
  - Service unit tests for `Client`, `Sala`, `Spectacol`, `Rezervare`
  - One controller test (`RezervareControllerTest`)

## Data model implemented

Entities found (6):

1. `Client`
2. `Sala`
3. `Loc`
4. `Spectacol`
5. `Rezervare`
6. `RezervareLoc`

Relations currently implemented:

- `@OneToMany` / `@ManyToOne`:
  - `Sala` -> `Loc`
  - `Sala` -> `Spectacol` (many shows in one hall, via `Spectacol.sala`)
  - `Client` -> `Rezervare` (via `Rezervare.client`)
  - `Spectacol` -> `Rezervare` (via `Rezervare.spectacol`)
  - `Rezervare` -> `RezervareLoc`
  - `Loc` -> `RezervareLoc`
- `@ManyToMany`: not directly implemented (currently modeled through join entity `RezervareLoc`)
- `@OneToOne`: not implemented

## API coverage (current)

Implemented endpoints:

- Clients:
  - `POST /api/clients`
  - `GET /api/clients`
  - `GET /api/clients/{id}`
- Sali:
  - `POST /api/sali`
- Spectacole:
  - `POST /api/spectacole`
  - `GET /api/spectacole`
- Rezervari:
  - `POST /api/rezervari`
  - `DELETE /api/rezervari/{id}`
  - `GET /api/rezervari/spectacol/{spectacolId}/locuri-disponibile`

Business rules already present:

- unique client email check
- auto seat generation for new hall
- reservation total price computed by seat count * show price
- seat conflict check for same show + active reservation
- cancellation marks reservation as `CANCELED`

---

## 2) What is missing vs mandatory requirements (60%)

Status legend: `DONE`, `PARTIAL`, `MISSING`, `BLOCKED`

### 1. Data model (10%)

- `PARTIAL` 6 entities exist and are interconnected
- `DONE` multiple `OneToMany/ManyToOne` relations exist
- `MISSING` explicit `OneToOne` relation (at least one required)
- `MISSING` explicit `ManyToMany` relation (at least one required by requirement wording)
- `PARTIAL` ER diagram exists in `README.md`, but must be updated to final model

### 2. Complete CRUD (8%)

- `PARTIAL` only create/read for some entities
- `MISSING` update/delete for most entities
- `PARTIAL` service layer exists
- `PARTIAL` exception handling exists but generic `RuntimeException` is overused
- `MISSING` entity-specific exceptions and cleaner HTTP status mapping

### 3. Multi-environment (5%)

- `MISSING` no Spring profiles (`dev`, `test`)
- `MISSING` no `application-dev.yml` / `application-test.yml`
- `MISSING` no H2 test profile configuration

### 4. Testing (7%)

- `BLOCKED` current test suite fails to compile
- `PARTIAL` unit tests exist but likely below target quality/coverage
- `MISSING` integration tests (minimum 3 end-to-end scenarios)
- `MISSING` measured coverage report and threshold

### 5. Views and validation (10%)

- `MISSING` no frontend pages/forms (REST only)
- `PARTIAL` server-side Bean Validation used in entities/DTOs
- `MISSING` client-side validation
- `MISSING` custom HTML error pages (404/500)

### 6. Logging (4%)

- `MISSING` no explicit logging strategy (`slf4j` usage in code absent)
- `MISSING` no separate error log file config

### 7. Pagination and sorting (6%)

- `MISSING` no pageable repository/service/controller endpoints
- `MISSING` no sorting by >=2 criteria for >=3 entities
- `MISSING` no UI pagination controls

### 8. Spring Security (10%)

- `MISSING` no auth/login/logout/roles
- `MISSING` no JDBC auth or user-role schema
- `MISSING` no endpoint protection by role

---

## 3) Optional microservices requirements (40%) status

Current status: `NOT STARTED`

- Still monolith, no service split yet
- No config server, discovery, gateway, load balancing, monitoring stack, JWT between services, resilience, NoSQL caching, or CI/CD pipeline

Recommendation: finish mandatory requirements solidly first, then split monolith into 3 services.

---

## 4) Key technical risks to address early

1. Broken test baseline (cannot trust regressions until fixed)
2. Spring Boot parent is `4.0.2` (new major; course/lab examples may target 3.x)
3. Package naming inconsistency (`spectacol` vs project name) can create confusion
4. Generic runtime exceptions reduce API clarity and grading quality
5. Potential N+1/performance issues in seat availability logic for larger datasets

---

## 5) Roadmap (simple execution plan)

## Phase A - Stabilize and clean foundation (immediate)

- [x] Fix test compile error in `RezervareControllerTest` import path
- [x] Make `./mvnw test` pass on clean checkout
- [x] Add code formatting + package naming decision (`spectacol` keep/rename) - keeping spectacol for now
- [ ] Add local run docs and environment variable strategy

## Phase B - Complete mandatory backend scope

- [x] Implement full CRUD for all entities (controller + service + repository methods)
  - [x] Created UpdateClientRequest, UpdateSalaRequest, UpdateSpectacolRequest DTOs
  - [x] Added update() and delete() methods to all services
  - [x] Added PUT and DELETE endpoints to all controllers
  - [x] All entities now have: POST (create), GET (read all/by id), PUT (update), DELETE
- [x] Add tests for all CRUD operations
  - [x] Service tests: 34 tests (`ClientServiceTest` 9, `SalaServiceTest` 8, `SpectacolServiceTest` 9, `RezervareServiceTest` 8)
  - [x] Controller WebMvc tests: 30 tests (`ClientControllerMvcTest` 8, `SalaControllerMvcTest` 7, `SpectacolControllerMvcTest` 7, `RezervareControllerMvcTest` 8)
  - [x] Direct unit controller tests removed (`ControllersTest`, `RezervareControllerTest`) to avoid duplicate coverage
  - [x] Total tests now: 64, all passing
- [ ] Add entity-specific exceptions (`NotFound`, `Conflict`, `Validation`, etc.)
- [ ] Improve global exception handler with consistent error payload
- [ ] Add pagination/sorting for at least 3 entities
- [ ] Add logging at service boundaries (INFO/DEBUG/ERROR)

## Phase C - Environment and testing quality

- [ ] Add profiles: `dev` (PostgreSQL), `test` (H2)
- [ ] Create `application-dev.yml` and `application-test.yml`
- [ ] Add integration tests (>=3 E2E scenarios)
- [ ] Raise service-layer test coverage toward >=70%
- [ ] Generate coverage report (JaCoCo)

## Phase D - Views and security

- [ ] Build UI stack (Thymeleaf recommended for faster delivery)
- [ ] Add CRUD forms + validation feedback
- [ ] Add custom error pages (`404`, `500`)
- [ ] Implement Spring Security with JDBC auth
- [ ] Add roles (`USER`, `ADMIN`) and endpoint authorization
- [ ] Add custom login + logout

## Phase E - Optional microservices for high grade

- [ ] Split into minimum 3 microservices
- [ ] Add service discovery + API gateway
- [ ] Add centralized config
- [ ] Add monitoring (Actuator + Prometheus + Grafana)
- [ ] Add resilience (circuit breaker + retry + fallback)
- [ ] Add one advanced pattern (Saga or CQRS)

---

## 6) Beautiful commit strategy (important)

Branching:

- `main` = stable/demo-ready
- `dev` = integration branch
- feature branches: `feat/<scope>`, fix branches: `fix/<scope>`

Commit rules:

- One logical change per commit
- Small atomic commits (prefer 50-200 lines net change where possible)
- Every commit compiles/tests in scope
- Message convention:

```text
type(scope): short imperative message
```

Examples:

- `fix(test): correct WebMvcTest import and restore green test compile`
- `feat(client): add update and delete endpoints with service logic`
- `feat(pagination): add pageable listing for clients, sali, spectacole`
- `refactor(errors): replace runtime exceptions with domain exceptions`
- `test(rezervare): add integration scenarios for seat conflict and cancellation`
- `docs(readme): update ER diagram and API usage examples`

PR checklist before merge:

- [ ] Build passes
- [ ] Relevant tests added/updated
- [ ] No unrelated file changes
- [ ] API/docs updated
- [ ] Commit messages clean and grouped

---

## 7) Suggested implementation order for next commits

1. `fix(test):` make baseline green (`mvn test`)
2. `chore(config):` introduce profiles + H2 test setup
3. `feat(crud):` complete missing CRUD for entities (batch by entity)
4. `feat(error-handling):` typed exceptions + improved global handler
5. `feat(logging):` logging config + usage in services
6. `feat(pagination):` pageable/sort endpoints and query params
7. `feat(ui):` Thymeleaf pages/forms + validation messages
8. `feat(security):` JDBC auth, roles, custom login/logout
9. `test(integration):` add 3+ end-to-end tests and coverage report
10. `docs(readme):` finalize architecture/setup/screenshots/contributions

---

## 8) Progress log template (keep updating)

Copy this section for each work session:

```text
Date:
Branch:
Goal:
Changes done:
Tests run + result:
Open issues:
Next commit(s):
```

---

## PROGRESS LOG

### Session 1 - 2026-06-17 (Test baseline + Update Operations)

**Date:** June 17, 2026  
**Goal:** 
1. Fix test compilation baseline (Step 1 of Phase A)
2. Implement full CRUD with update operations (Step 1 of Phase B)

**Changes done:**
- Fixed `RezervareControllerTest` import issue + refactored to unit test pattern
- Added `spring-boot-test-autoconfigure` dependency to pom.xml
- Created 3 Update DTOs: `UpdateClientRequest`, `UpdateSalaRequest`, `UpdateSpectacolRequest`
- Added full CRUD to ClientService/Controller (create, read all, read by id, update, delete)
- Added full CRUD to SalaService/Controller (create, read all, read by id, update, delete)
- Added full CRUD to SpectacolService/Controller (create, read all, read by id, update, delete)
- Enhanced RezervareService with: getAll(), getById(), updateReservationSeats()
- Enhanced RezervareController with corresponding PUT/GET endpoints

**Test results:**
- ✅ BUILD SUCCESS
- ✅ All 64 tests pass (30 controller WebMvc + 34 service)
- ✅ Controller coverage now focuses on `@WebMvcTest` tests only
- No failures, no errors

**API coverage after this step:**
```
Clients:
  POST   /api/clients              (create)
  GET    /api/clients              (read all)
  GET    /api/clients/{id}         (read by id)
  PUT    /api/clients/{id}         (update)
  DELETE /api/clients/{id}         (delete)

Sali (Halls):
  POST   /api/sali                 (create)
  GET    /api/sali                 (read all)
  GET    /api/sali/{id}            (read by id)
  PUT    /api/sali/{id}            (update)
  DELETE /api/sali/{id}            (delete)

Spectacole (Shows):
  POST   /api/spectacole           (create)
  GET    /api/spectacole           (read all)
  GET    /api/spectacole/{id}      (read by id)
  PUT    /api/spectacole/{id}      (update)
  DELETE /api/spectacole/{id}      (delete)

Rezervari (Reservations):
  POST   /api/rezervari             (create)
  GET    /api/rezervari             (read all)
  GET    /api/rezervari/{id}        (read by id)
  PUT    /api/rezervari/{id}/seats  (update seats)
  DELETE /api/rezervari/{id}        (cancel)
  GET    /api/rezervari/spectacol/{id}/locuri-disponibile (available seats)
```

**Completed requirement:** CRUD Operations (8%) - ✅ DONE

**Next steps:** 
1. Spring Security (10% requirement, highest impact)
2. Profiles + H2 test config (5%)
3. Pagination (6%)
4. Logging (4%)

---

## 9) Notes for final delivery

- Keep `README.md` public and polished (architecture, setup, screenshots, API)
- Track team member contributions continuously (do not leave for final week)
- Keep deployment ready in parallel (Docker Compose first, cloud after)
- Keep demo script ready (happy path + failure path + security)

