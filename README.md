# 🎮 Tic-Tac-Toe REST API & Web UI

A full-stack Tic-Tac-Toe application. The project evolved from a simple console game into a secure REST API with a database, JWT authentication, multiplayer support, and advanced analytics.

## 🚀 Features

*   **Play vs AI:** An unbeatable computer opponent powered by the Minimax algorithm.
*   **Multiplayer (PvP):** Create network games and join matches with other players.
*   **JWT Authentication:** Secure endpoints using Access and Refresh tokens (Spring Security).
*   **Database Integration:** Persistent storage for users, games, and history using PostgreSQL (Spring Data JPA + Hibernate).
*   **Game History:** View all completed matches for the current user.
*   **Leaderboard:** Player rankings sorted by win ratio (powered by complex SQL queries).
*   **Dark Web UI:** A clean, minimalist, and responsive web interface to play directly in the browser.

## 🛠 Tech Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 3 (Web, Security, Data JPA)
*   **Database:** PostgreSQL
*   **Authentication:** JSON Web Tokens (JJWT)
*   **Build Tool:** Gradle (Kotlin DSL)
*   **Testing:** JUnit 5, Mockito
*   **Frontend:** HTML5, CSS3, Vanilla JavaScript

## 📂 Architecture

The project follows Clean Architecture principles and is divided into layers:
*   **`domain`** — Core application. Models (`Game`, `User`), business logic (`GameServiceImpl`, `UserServiceImpl`), AI bot (`MinimaxBotService`), and game rules.
*   **`datasource`** — Data layer. JPA entities, Spring Data repositories, and mappers between DB and domain.
*   **`web`** — API layer. Controllers (`GameController`, `AuthController`), DTOs, JWT filters (`AuthFilter`), and web mappers.
*   **`di`** — Spring Configuration. Application dependency graph wiring.

## 🏃‍♂️ How to Run

### 1. Database Setup (Docker)
Start the PostgreSQL container:
```bash
docker run --name tictactoe-db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```
Create the database:
```bash
docker exec -it tictactoe-db psql -U postgres -c "CREATE DATABASE tictactoe_db;"
```

### 2. Application Configuration
Configure your database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tictactoe_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Run the App
```bash
./gradlew bootRun
```
The application will be available at: **http://localhost:8080**

## 📡 API Endpoints

### Authentication (`/auth`)
*   `POST /auth/register` — Register a new user.
*   `POST /auth/login` — Log in (returns `accessToken` and `refreshToken`).
*   `POST /auth/refresh` — Refresh an expired Access token using a Refresh token.

### Games (`/game`) — Require JWT token (`Authorization: Bearer <token>`)
*   `POST /game/bot` — Create a game against the computer.
*   `POST /game/multiplayer` — Create a multiplayer game.
*   `POST /game/{id}/join` — Join an existing multiplayer game.
*   `POST /game/{id}` — Make a move (submit the updated board).
*   `GET /game/{id}` — Get the current state of a game.
*   `GET /game/available` — List available multiplayer games.
*   `GET /game/history` — Get the match history for the current user.
*   `GET /game/leaderboard?limit=10` — Get the top players leaderboard.
*   `DELETE /game/{id}` — Cancel a game you created (if no opponent has joined yet).

### Users (`/user`)
*   `GET /user/{id}` — Get user information by UUID.

## 🧪 Testing

The project is covered with unit tests (using Mockito to isolate business logic).
Run the tests:
```bash
./gradlew test
```