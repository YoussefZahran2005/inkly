<div align="center">

# Inkly

**A Spring Boot blog platform API with stateless JWT authentication.**

![Java](https://img.shields.io/badge/Java-25-1f2937?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-1f2937?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-1f2937?style=for-the-badge)
![Maven](https://img.shields.io/badge/Build-Maven-1f2937?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-V1_in_progress-6d28d9?style=for-the-badge)

</div>

---

## Overview

Inkly is the backend of a blogging platform. It lets authenticated users write posts, organize them with categories and tags, and keep drafts private-ish until they are ready to publish, while anyone can browse the published content.

This is **V1**: a clean, layered REST API that covers the essentials. There is no frontend in this repository, and a fair amount of the roadmap is still ahead (see [Roadmap](#roadmap)).

> **A note on origins.** The idea comes from the DevTiro YouTube channel, and a big thank-you goes to him for the excellent tutorial. The code in this repository was largely written by me while following along, which is the best way I know to actually learn how all the layers fit together.

## Features

- Login with email and password, receiving a JWT in return
- Public listing and retrieval of published posts, categories, and tags
- Authenticated create, update, and delete for posts, categories, and tags
- Authenticated listing of the current user's draft posts
- Filtering published posts by category, tag, or both
- Category and tag deletion guards when posts are still associated with them
- Automatic `createdAt` / `updatedAt` timestamps and computed reading time (`ceil(wordCount / 200)`)
- A development seed user created at startup if missing

## Tech Stack

| Area | Technology |
| --- | --- |
| Runtime | Java 25 |
| Framework | Spring Boot 4.1.1 (`spring-boot-starter-webmvc`) |
| Persistence | Spring Data JPA, Hibernate |
| Validation | Jakarta Bean Validation |
| Security | Spring Security, stateless JWT |
| JWT | `io.jsonwebtoken:jjwt-*` 0.11.5 |
| Mapping | MapStruct 1.6.3 |
| Boilerplate | Lombok 1.18.44 |
| Databases | PostgreSQL (main), H2 (tests) |
| Build | Maven wrapper |

## Architecture

Inkly follows a classic layered Spring structure:

```text
src/main/java/com/zahran/blog_platform
├── controller        HTTP endpoints, DTO validation and mapping
├── service           Service interfaces
│   └── impl          Business workflows and persistence coordination
├── repository        Spring Data JPA interfaces
├── domain
│   ├── entity        JPA model (User, Post, Category, Tag)
│   └── dtos          Request and response DTOs
├── mapper            MapStruct mappers between entities and DTOs
├── config            Security configuration
└── security          JWT filter and user details
```

### Request lifecycle

1. A request hits a controller mapping under `/api/v1/...`.
2. `JwtAuthFilter` runs before `UsernamePasswordAuthenticationFilter`. If a valid bearer token is present, it sets the Spring Security authentication and stores `userId` as a request attribute.
3. The `SecurityFilterChain` allows public routes and requires authentication for everything else.
4. The controller validates the DTO (where `@Valid` is used) and calls a service.
5. The service applies business rules and talks to repositories.
6. MapStruct converts entities back to DTOs, and `ErrorController` (a `@ControllerAdvice`) turns exceptions into `ApiErrorResponse` bodies.

### Data model

- **User** owns many **Posts**
- **Post** belongs to one **Category** and has many **Tags** (through the `post_tags` join table)
- **PostStatus** is either `DRAFT` or `PUBLISHED`
- All IDs are UUIDs

## API Reference

Base path: `/api/v1`

| Method | Route | Auth | Description |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Public | Authenticate with email and password, receive a JWT |
| `GET` | `/posts` | Public | List published posts; optional `categoryId` and `tagId` filters |
| `GET` | `/posts/drafts` | Required | List the authenticated user's drafts |
| `GET` | `/posts/{id}` | Public | Get a post by ID |
| `POST` | `/posts` | Required | Create a post |
| `PUT` | `/posts/{id}` | Required | Update a post |
| `DELETE` | `/posts/{id}` | Required | Delete a post |
| `GET` | `/categories` | Public | List categories with published post counts |
| `POST` | `/categories` | Required | Create a category |
| `DELETE` | `/categories/{id}` | Required | Delete a category (blocked if it has posts) |
| `GET` | `/tags` | Public | List tags with published post counts |
| `POST` | `/tags` | Required | Create missing tags, return new and existing matches |
| `DELETE` | `/tags/{id}` | Required | Delete a tag (blocked if it has posts) |

Authenticated requests send the token as a header:

```http
Authorization: Bearer <token>
```

<details>
<summary><strong>Request details and validation rules</strong></summary>

<br>

**Login** (`POST /api/v1/auth/login`)

- Body: `email`, `password`
- Response: `token`, `expiersIn` (the field name is misspelled in the current DTO; see known issues)
- `401 Unauthorized` on bad credentials

**Create / update post** (`POST /api/v1/posts`, `PUT /api/v1/posts/{id}`)

- Body: `title`, `content`, `categoryId`, `tagIds`, `status` (update also requires `id` in the body)
- Title: required, 3 to 200 characters
- Content: required, 10 to 50000 characters
- Category ID: required
- Tags: at most 10
- Status: required (`DRAFT` or `PUBLISHED`)
- The author is the authenticated user; the category and all tags must already exist

**Create category** (`POST /api/v1/categories`)

- Body: `name`
- Required, 2 to 50 characters; letters, numbers, underscores, spaces, and hyphens only
- Duplicate names (case-insensitive) return `400 Bad Request`

**Create tags** (`POST /api/v1/tags`)

- Body: `names`, a set of tag names
- Existing exact-name matches are returned; missing ones are created

**Errors**

| Exception | Status |
| --- | --- |
| `IllegalArgumentException` | `400 Bad Request` |
| `BadCredentialsException` | `401 Unauthorized` |
| `EntityNotFoundException` | `404 Not Found` |
| `IllegalStateException` | `409 Conflict` |
| Anything else | `500 Internal Server Error` |

</details>

## Getting Started

### Prerequisites

- JDK 25
- Docker (for the bundled PostgreSQL and Adminer containers)

Maven is not required, since the project ships with the Maven wrapper.

### 1. Start the database

```bash
docker compose up -d
```

This starts PostgreSQL on port `5432` and Adminer on port `8888`. The compose file only sets `POSTGRES_PASSWORD`, so PostgreSQL's defaults apply for the username and database name.

### 2. Check the configuration

The main `application.properties` points at `jdbc:postgresql://localhost:5432/postgres` with the `postgres` user. Make sure `spring.datasource.password` matches the password set in `docker-compose.yml`, and set `jwt.secret` to a long enough HMAC key for JJWT.

These values live directly in the properties file, which is fine for local development but should never be reused as-is anywhere real.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The schema is created and updated by Hibernate (`ddl-auto=update`), and a development seed user is created on first startup if it does not already exist.

### 4. Try it

```bash
# Log in and grab a token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "<your-email>", "password": "<your-password>"}'

# List published posts (no token needed)
curl http://localhost:8080/api/v1/posts
```

> The seed user's credentials are defined in `SecurityConfiguration`. The port above assumes Spring Boot's default of `8080`.

## Testing

The only test at the moment is `BlogPlatformApplicationTests.contextLoads()`, which verifies that the application context starts against an in-memory H2 database.

```bash
./mvnw test
```

Controller, service, repository, and security tests are not written yet.

## Known Limitations

This is a V1, and it is better to be upfront about what is still rough:

| Area | Current behavior |
| --- | --- |
| Authorization | Every authenticated user has `ROLE_USER`; post update and delete do not check ownership |
| Drafts | `GET /posts/{id}` returns a post regardless of status, so drafts can be fetched by ID |
| Registration | No registration endpoint; users come from the seed user |
| Tag validation | `@Valid` is missing on `POST /tags`, so its validation annotations may not run |
| Validation errors | No dedicated handler for validation failures in the `ApiErrorResponse` format |
| Login response | `expiersIn` is misspelled, and the returned value (`84600`) does not match the 24-hour token expiry |
| Deletes | Deleting a missing category or tag returns `204` instead of an error |
| Migrations | Schema is managed by Hibernate `ddl-auto`; no Flyway or Liquibase |
| API docs | No OpenAPI / Swagger documentation yet |

## Roadmap

Planned for future versions, added gradually:

- Proper authorization (roles and ownership checks)
- Refresh tokens
- Unit and integration testing
- Dockerizing the application itself
- And more as the project grows

## Credits

Inspired by the **DevTiro** YouTube channel. Thank you for the idea and the great teaching.

---

<div align="center">

Built by Zahran, one layer at a time.

</div>