# Authentication System (JWT)

This project is a full auth backend built with Spring Boot.
It supports register/login, secure password hashing, JWT authentication, and protected APIs like profile and feed.

This README is written for both backend and frontend usage so you can run the app end-to-end and deploy it cleanly.

## What this project does

- User registration and login
- Password hashing with BCrypt
- JWT token generation and validation
- Protected APIs (`/profile`, `/feed`) that require a valid Bearer token
- Profile update API (`PUT /profile`)
- Docker-based deployment for Render

## Tech stack

Backend:
- Java 21
- Spring Boot (Web, Security, Validation, JPA)
- JWT (`jjwt`)
- PostgreSQL (production) / H2 (local default)

Frontend (expected setup):
- React + Vite
- Axios for API calls
- Token stored in local storage and attached as `Authorization: Bearer <token>`

## API quick reference

Public:
- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/health`

Protected (JWT required):
- `GET /profile`
- `PUT /profile`
- `GET /feed`

## Typical auth flow (frontend + backend)

1. User signs up via `POST /auth/register`.
2. Backend stores password as BCrypt hash.
3. User logs in via `POST /auth/login` and receives JWT.
4. Frontend stores token (usually local storage).
5. Frontend sends token in every protected request:
   - `Authorization: Bearer <token>`
6. Backend validates token in security filter and allows/denies access.

## Environment variables

You can run locally with defaults, then override these in Render:

- `PORT` (default: `8080`)
- `JWT_SECRET` (base64 string; set a strong value in production)
- `JWT_EXPIRATION_MS` (default: `3600000`)
- `CORS_ALLOWED_ORIGINS` (default: `http://localhost:5173`)
- `CORS_ALLOWED_ORIGIN_PATTERNS` (supports ngrok-style domains)
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_DRIVER` (local default: `org.h2.Driver`, production: `org.postgresql.Driver`)
- `JPA_DDL_AUTO` (default: `update`)
- `H2_CONSOLE_ENABLED` (default: `false`)

## Local backend run

```powershell
.\mvnw.cmd spring-boot:run
```

## Run tests

```powershell
.\mvnw.cmd test
```

## Docker run (backend)

```powershell
docker build -t auth-system .
docker run --rm -p 8080:8080 auth-system
```

## Frontend integration notes (React + Vite)

Set frontend env:

- `VITE_API_URL=https://<your-backend-domain>`

Example Axios usage:

```js
const token = localStorage.getItem("token");

await axios.get(`${import.meta.env.VITE_API_URL}/profile`, {
  headers: {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json"
  }
});
```

If you are testing through ngrok and see CORS/network issues, add:

```js
"ngrok-skip-browser-warning": "true"
```

## Deployment guide

### Backend on Render (Docker)

This repo already contains:
- `Dockerfile`
- `render.yaml` (starter blueprint)

Set these Render env vars at minimum:
- `JWT_SECRET`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_DRIVER=org.postgresql.Driver`
- `CORS_ALLOWED_ORIGINS=https://<your-frontend-domain>`

### Frontend on Vercel or Netlify

Set:
- `VITE_API_URL=https://<your-render-backend>.onrender.com`

Then deploy normally from your frontend repo.

## Project structure (backend)

- `src/main/java/com/auth/controller` - REST controllers (`auth`, `profile`, `feed`, health)
- `src/main/java/com/auth/service` - business logic
- `src/main/java/com/auth/security` - JWT service + auth filter
- `src/main/java/com/auth/config` - Spring Security + CORS setup
- `src/main/java/com/auth/model` - JPA entities
- `src/main/java/com/auth/repository` - JPA repositories
- `src/main/java/com/auth/dto` - request/response models

## Known current behavior

- `/feed` currently returns sample items (protected by JWT).
- `/profile` supports both read and update for authenticated user.
- Security is stateless; no server-side session.

## Next improvements (optional)

- Add refresh tokens
- Add role-based authorization
- Add DB migrations (Flyway)
- Add OpenAPI/Swagger docs

