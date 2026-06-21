# AGENTS.md - Project Status & Instructions for Automated Agents

> This file is for agent reference only. Keep it updated after each major change.
> Last updated: 2026-06-17 (After MockMvc-only controller testing cleanup)

## 🎯 Current Project Status

**Project:** Theater Reservation System (Spring Boot Microservices Architecture Track)  
**Course:** AWBD Master Program (Web Applications with Microservices Architecture)  
**Grade Target:** 8-9/10 (60% mandatory + 30% optional microservices)  
**Current Completion:** ~40% (baseline + CRUD complete, security/profiles/testing/logging pending)

---

## ✅ What's DONE

### Phase A: Foundation (COMPLETE)
- [x] Test baseline green (9/9 tests passing)
- [x] Fixed `RezervareControllerTest` import issues
- [x] Added `spring-boot-test-autoconfigure` dependency

### Phase B: CRUD Operations (COMPLETE - 8% of 60%)
- [x] Full CRUD for all entities
  - `Client` → POST, GET all, GET by id, PUT, DELETE
  - `Sala` → POST, GET all, GET by id, PUT, DELETE
  - `Spectacol` → POST, GET all, GET by id, PUT, DELETE
  - `Rezervare` → POST, GET all, GET by id, PUT (seats), DELETE (cancel)
- [x] Created update request DTOs: `UpdateClientRequest`, `UpdateSalaRequest`, `UpdateSpectacolRequest`
- [x] All services have proper `getById()`, `update()`, `delete()` methods
- [x] All controllers have PUT and DELETE endpoints
- [x] Full tests updated: 64 tests, all passing
  - Service tests: 34 (`ClientServiceTest` 9, `SalaServiceTest` 8, `SpectacolServiceTest` 9, `RezervareServiceTest` 8)
  - Controller WebMvc tests: 30 (`ClientControllerMvcTest` 8, `SalaControllerMvcTest` 7, `SpectacolControllerMvcTest` 7, `RezervareControllerMvcTest` 8)
  - Removed duplicate direct unit controller tests (`ControllersTest`, `RezervareControllerTest`)

---

## ⏳ What's NEXT (Priority Order)

### Immediate Next (High Value, Doable Soon):

1. **Spring Security (10% requirement)** ← HIGHEST PRIORITY
   - Create `SecurityConfig.java` with `@EnableWebSecurity`
   - Implement JDBC-based user authentication
   - Add role system (ADMIN, USER)
   - Protect endpoints by role
   - Create custom login page (Thymeleaf)
   - Estimated time: 2-3 hours

2. **Multi-Environment Profiles (5% requirement)**
   - Create `application-dev.yml` (PostgreSQL)
   - Create `application-test.yml` (H2 in-memory)
   - Add Maven profile activation
   - Estimated time: 30 minutes

3. **Pagination & Sorting (6% requirement)**
   - Add Spring Data `Pageable` to repositories
   - Update all list endpoints to return `Page<Entity>` instead of `List<Entity>`
   - Add `@Param` annotations for sorting
   - Estimated time: 1 hour

4. **Logging Configuration (4% requirement)**
   - Create `logback.xml` in `src/main/resources`
   - Add `@Slf4j` annotations to all services
   - Add log statements at method entry/exit and error points
   - Configure separate error log file
   - Estimated time: 45 minutes

5. **Exception Handling Improvements**
   - Create custom exception classes: `EntityNotFoundException`, `DuplicateEmailException`, `SeatAlreadyReservedException`
   - Replace generic `RuntimeException` with domain exceptions
   - Update `GlobalExceptionHandler` with better error payloads
   - Estimated time: 1 hour

6. **Integration Tests (7% requirement)**
   - Create `@SpringBootTest` test class
   - Implement 3+ end-to-end test scenarios
   - Add test data fixtures
   - Estimated time: 2 hours

7. **Views/Frontend (10% requirement)**
   - Add Thymeleaf views for each CRUD operation
   - Create forms for client, sala, spectacol, reservation creation
   - Add validation error messages
   - Estimated time: 3-4 hours

---

## 📝 Important Notes for Agents

### Code Organization
- Package structure: `com.example.spectacol.*`
  - `model/` - JPA entities with validation annotations
  - `repository/` - Spring Data JPA interfaces
  - `service/` - Business logic (current focus for logging)
  - `controller/` - REST endpoints
  - `dto/` - Request/Response objects
  - `exception/` - Exception handling classes

### Testing Approach
- Current tests: Service unit tests + controller WebMvc tests
- Use `@Mock` + `@InjectMocks` for service tests
- Use `@WebMvcTest` + `@MockitoBean` for controller HTTP tests
- Keep `GlobalExceptionHandler` imported in WebMvc tests for error payload assertions
- Future: Add `@SpringBootTest` for integration tests with real database

### Database & Configuration
- Default: PostgreSQL (dev profile)
- Test: H2 in-memory (test profile)
- Connection properties in `application.properties` (dev) and `application-test.yml` (test)

### Important Files to Edit
- `pom.xml` - Dependencies
- `src/main/java/com/example/spectacol/service/*` - Add logging here
- `src/main/java/com/example/spectacol/controller/*` - Update for pagination
- `src/main/resources/application*.properties/yml` - Configuration
- `README_LOCAL_PLAN.md` - Keep this updated after each major change

### Test Command
```bash
./mvnw clean test
```
Should always return: **BUILD SUCCESS** with all tests passing.

### Build Command
```bash
./mvnw clean package -DskipTests
```

---

## 💾 Recent Commit Strategy

All commits follow this message format:

```
type(scope): short imperative message

- Detailed point 1
- Detailed point 2
```

Example for this session:
```
test(controller): keep WebMvc tests and remove duplicate unit controller suites

- Keep per-controller WebMvc tests for Client, Sala, Spectacol, Rezervare
- Remove ControllersTest and RezervareControllerTest as duplicate coverage
- Verify full suite remains green with 64 tests
```

---

## 🔍 Checklist for Next Agent

When picking up this project:

- [ ] Read `README_LOCAL_PLAN.md` for detailed roadmap
- [ ] Check `git log` to see recent work
- [ ] Run `./mvnw clean test` to verify baseline is green
- [ ] Review the task in this AGENTS.md file
- [ ] Implement the feature/fix
- [ ] Run tests again
- [ ] Update this AGENTS.md and README_LOCAL_PLAN.md
- [ ] Prepare commit message (DON'T commit, just provide message)

---

## 📊 Grading Coverage Checklist

**Mandatory (60%):**
- [x] Data Model (10%) - 6 entities with proper relations
- [x] CRUD Operations (8%) - Full CRUD for all entities
- [ ] Multi-environment (5%) - Profiles needed
- [ ] Testing (7%) - Unit tests exist, integration tests needed
- [ ] Views & Validation (10%) - Only server-side validation, no views yet
- [ ] Logging (4%) - Not started
- [ ] Pagination & Sorting (6%) - Not started
- [ ] Spring Security (10%) - Not started

**Optional (40%):**
- [ ] Config Server (4%)
- [ ] Service Discovery (6%)
- [ ] Load Balancing (5%)
- [ ] API Gateway (4%)
- [ ] Monitoring (5%)
- [ ] Distributed Security (4%)
- [ ] Resilience (5%)
- [ ] Design Patterns (3%)
- [ ] NoSQL & Caching (4%)

---

## 🚀 Quick Start Commands

```bash
# Run tests
./mvnw test

# Build project
./mvnw clean package -DskipTests

# Run application (if configured)
./mvnw spring-boot:run

# Check test coverage (after adding JaCoCo)
./mvnw jacoco:report
```

---

## 📞 Key Contacts/Dependencies

- **Database:** PostgreSQL (local dev), H2 (test)
- **Testing:** JUnit 5, Mockito
- **Build:** Maven Wrapper (mvnw)
- **Java Version:** 17
- **Spring Boot:** 4.0.2

---

## 📌 Next Session Instructions

**If continuing work on Spring Security:**
1. Create `config/SecurityConfig.java` class
2. Extend `UserDetailsService` for JDBC authentication
3. Add role-based access control decorators
4. Update all endpoints with `@PreAuthorize` or `@RolesAllowed`
5. Create login/logout controllers and views

**If continuing work on Profiles:**
1. Rename `application.properties` to `application-dev.yml`
2. Create `application-test.yml` with H2 configuration
3. Add Maven profile activation to `pom.xml`
4. Test with: `./mvnw test -Dspring.profiles.active=test`

---

Last updated: 2026-06-17 after WebMvcTest cleanup and controller test deduplication.
