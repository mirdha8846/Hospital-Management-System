# Hospital Management System (HMS)

## Project Overview
- **Short summary:** HMS is a Java Spring Boot backend that provides user authentication (email + OAuth2), role-based access control (Admin / Doctor / Patient), and core hospital features such as patient profiles, doctor onboarding, and appointment management.
- **Primary goal:** Demonstrate a production-like Spring Boot service with JWT authentication, OAuth2 login flows, layered architecture (controllers → services → repositories), and JPA entity modeling for doctors, patients, appointments and insurance.

## Tech Stack
- Java 21
- Spring Boot 4 (Web MVC, Spring Data JPA, Spring Security, OAuth2 client)
- PostgreSQL (runtime)
- JJWT (JWT handling)
- ModelMapper (DTO mapping)
- Lombok
- Maven build

## What I inspected
- Project descriptor: [pom.xml](pom.xml)
- Application entry: [src/main/java/com/example/hms/HmsApplication.java](src/main/java/com/example/hms/HmsApplication.java)
- Auth flow: [src/main/java/com/example/hms/controller/Authcontroller.java](src/main/java/com/example/hms/controller/Authcontroller.java) and [src/main/java/com/example/hms/security/Authservice.java](src/main/java/com/example/hms/security/Authservice.java)
- JWT filter: [src/main/java/com/example/hms/security/JwtAuthFilter.java](src/main/java/com/example/hms/security/JwtAuthFilter.java)
- Example endpoints: [src/main/java/com/example/hms/controller/PatientController.java](src/main/java/com/example/hms/controller/PatientController.java) and [src/main/java/com/example/hms/controller/Doctorcontroller.java](src/main/java/com/example/hms/controller/Doctorcontroller.java)
- Core entities: [src/main/java/com/example/hms/entity/User.java](src/main/java/com/example/hms/entity/User.java), [src/main/java/com/example/hms/entity/Patient.java](src/main/java/com/example/hms/entity/Patient.java), [src/main/java/com/example/hms/entity/Doctor.java](src/main/java/com/example/hms/entity/Doctor.java), [src/main/java/com/example/hms/entity/Appointment.java](src/main/java/com/example/hms/entity/Appointment.java)
- Config: [src/main/resources/application.properties](src/main/resources/application.properties) and [src/main/resources/application.yaml](src/main/resources/application.yaml)

## Architecture & Key Concepts
- Layered design: Controllers handle HTTP, Services implement business logic, Repositories (Spring Data JPA) persist entities.
- Authentication:
  - JWT-based access tokens generated via `Authutil` (used by `Authservice`).
  - `JwtAuthFilter` extracts the `Authorization: Bearer ...` header, validates the token and populates Spring Security context with a `User` principal.
  - OAuth2 login support (Google/GitHub/Twitter placeholders) using Spring OAuth2 client and `OAuth2SuccessHandler`.
- Authorization: `User` stores a set of `RoleType` roles. Permissions are derived from `RoleToPermissionMap` and converted to Spring `GrantedAuthority` values.
- Domain model highlights:
  - `User` (implements `UserDetails`) — core application user with roles and provider info and a DB index on provider fields (see `User` entity).
  - `Patient` — linked 1:1 to `User`, stores patient-specific details and appointments.
  - `Doctor` — linked to `User`, has specialization and appointments.
  - `Appointment` — links `Patient` and `Doctor` with a scheduled time and reason.

## Main Features / Endpoints (examples)
- Authentication
  - POST `/auth/signup` — create new user (email signup). See `Authcontroller`.
  - POST `/auth/login` — authenticate and receive JWT access token.
  - OAuth2 flows — configured in `application.yaml` (Google/GitHub/Twitter registration placeholders).

- Patients
  - POST `/patients/appointments` — create new appointment (request DTO: `CreateAppointRequestDto`).
  - GET `/patients/profile` — fetch patient profile (example uses a static id in controller; replace with authenticated id in production).

- Doctors
  - GET `/doctors/appointments` — fetch appointments for currently-authenticated doctor (uses `SecurityContextHolder` to obtain the `User`).

## Implementation Highlights (showcase points for interviews)
- Indexing & performance:
  - `User` entity defines a DB index on `providerId, providerType` to speed up OAuth lookups and uniqueness checks. Mention how adding targeted indexes (e.g., on email columns, foreign keys) improves read performance under load.
- Pagination & custom queries:
  - Services use Spring Data pagination patterns. Example: `Patientservice#getAllPatients(Integer pagenumber, Integer pagesize)` calls `patientrepo.findAllPatitent(PageRequest.of(pagenumber, pagesize))` — this demonstrates server-side paging for large result sets.
  - The code uses repository abstraction for custom queries (see repository methods under `src/main/java/com/example/hms/repo`). Point out how you can implement `@Query` JPQL or derived query methods for domain-specific filters.
- Transaction management & consistency:
  - Service methods are annotated with `@Transactional` (e.g., `Patientservice#getPatientById`) to ensure atomic operations and consistent state when modifying multiple entities.
- Cascade, orphan removal & fetch strategies:
  - Entities use `cascade` and `orphanRemoval` where appropriate (e.g., `Patient` → `Insurance`, `appointments` with `CascadeType.REMOVE`) and a mix of `EAGER`/`LAZY` fetch to balance convenience and performance. Be ready to explain the trade-offs.
- DTO mapping & separation of concerns:
  - ModelMapper is used to convert entities to DTOs (`PatientResponseDto`, `DoctorDto`, etc.), keeping controllers decoupled from persistence models.
- Security & token flow:
  - Custom `JwtAuthFilter` validates tokens and populates Spring Security context.
  - `Authservice` demonstrates login, signup, and OAuth account linking flows (provider id lookups, username/email reconciliation).
- Role-based authorities & permissions:
  - `User#getAuthorities()` maps roles via `RoleToPermissionMap` into `GrantedAuthority`s and adds `ROLE_` prefixed authorities, showcasing a flexible RBAC approach.
- Error handling and best practices:
  - Project contains `erros` package (e.g., `ApiError`, `GlobalExpection`) for centralized exception handling — useful to discuss standardizing error responses.

## How to build & run (local)
1. Configure PostgreSQL and update connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<password>
jwt.secretkey="your-jwt-secret"
```

2. Build and run using Maven:

```bash
mvn clean package
mvn spring-boot:run
```

Or run the generated jar:

```bash
java -jar target/hms-0.0.1-SNAPSHOT.jar
```

3. Test endpoints with curl / Postman (examples):

```bash
curl -X POST http://localhost:8080/auth/signup -H "Content-Type: application/json" -d '{"username":"patient@example.com","password":"P@ssword1","name":"John Doe","roles":["PATIENT"]}'

curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"patient@example.com","password":"P@ssword1"}'

curl -X POST http://localhost:8080/patients/appointments -H "Content-Type: application/json" -H "Authorization: Bearer <token>" -d '{"patientId":4,"doctorId":2,"appointmentTime":"2026-03-01T10:00:00","reason":"General checkup"}'
```

## Important configuration & notes
- Java version configured in `pom.xml` is 21.
- JWT secret: set `jwt.secretkey` in properties or environment for production use.
- OAuth2 clients in `application.yaml` use placeholders — replace with real credentials and callback URLs.
- Lombok: ensure IDE supports Lombok annotations.
