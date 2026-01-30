# Chesslytics Backend

A lean Spring Boot application for ingesting and analyzing chess game data from Chess.com and Lichess.

## Architecture

The backend is split into **separate applications** for different concerns:

1. **API Application** - REST API server for querying and analytics (runs as a service)
2. **Ingest Application** - Batch job for data ingestion (runs once and exits)

This separation provides:
- **Security**: No REST endpoint for ingestion
- **Scalability**: Ingest can be scheduled externally (cron, K8s CronJob)
- **Clean separation**: Each app has minimal dependencies

## Quick Start

### Prerequisites
- Java 17+
- Gradle (wrapper included)

### Run the API Server

```bash
# Start the REST API (default)
./gradlew :backend:bootRun

# Or explicitly
./gradlew :backend:api
```

The API will be available at `http://localhost:8080`

### Run Data Ingestion

```bash
# Ingest Chess.com player data
./gradlew :backend:ingest -Pusername=hikaru

# Or another player
./gradlew :backend:ingest -Pusername=magnuscarlsen
```

The ingest app runs as a batch job and exits when complete.

## API Endpoints

### Query Games
```bash
# List all games (paginated)
GET /api/games?page=0&size=20

# Get a specific game
GET /api/games/{id}
```

**Example:**
```bash
curl http://localhost:8080/api/games?page=0&size=10
curl http://localhost:8080/api/games/1
```

## Configuration

### Database

**Default:** SQLite (file: `chesslytics.db`)
- Zero configuration needed
- Perfect for development and small deployments
- File-based, portable database

**PostgreSQL** (recommended for production):
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/chesslytics
export DATABASE_USER=postgres
export DATABASE_PASSWORD=yourpassword
export DATABASE_DRIVER=org.postgresql.Driver
```

Or in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chesslytics
    username: postgres
    password: yourpassword
    driver-class-name: org.postgresql.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

**Custom SQLite location:**
```bash
export DATABASE_URL=jdbc:sqlite:/path/to/your/database.db
```

### Custom Configuration

Edit `src/main/resources/application.yml`:
```yaml
chesslytics:
  chesscom:
    base-url: https://api.chess.com/pub
  lichess:
    base-url: https://lichess.org/api
```

## Project Structure

```
backend/
├── src/main/java/io/tglanz/chesslytics/backend/
│   ├── app/                                # Application entry points
│   │   ├── ChesslyticsApiApplication.java      # REST API server
│   │   └── ChesslyticsIngestApplication.java   # Batch ingestion
│   ├── controller/                         # REST controllers
│   │   └── GameController.java
│   ├── service/                            # Business logic
│   │   └── ChessComIngestionService.java
│   ├── repository/                         # Spring Data JPA repositories
│   │   ├── AccountRepository.java
│   │   ├── GameRepository.java
│   │   └── GamePlayerRepository.java
│   ├── model/                              # JPA entities
│   │   ├── Account.java
│   │   ├── Game.java
│   │   ├── GamePlayer.java
│   │   └── ...
│   └── chesscom/                           # External API clients
│       ├── ChessComClient.java
│       └── *DTO.java                       # Data transfer objects
└── src/main/resources/
    ├── application.yml                     # Main configuration
    └── application-ingest.yml              # Ingest-specific config
```

## Development

### Code Quality

**Spotless** (Google Java Format):
```bash
# Format code
./gradlew :backend:spotlessApply

# Check formatting
./gradlew :backend:spotlessCheck
```

**Checkstyle**:
```bash
# Run checkstyle
./gradlew :backend:checkstyleMain

# View report
open backend/build/reports/checkstyle/main.html
```

### Build

```bash
# Full build with code quality checks
./gradlew :backend:build

# Build without tests
./gradlew :backend:build -x test
```

### Running Tests

```bash
./gradlew :backend:test
```

## Deployment

### Production API Server

```bash
# Build JAR
./gradlew :backend:bootJar

# Run
java -jar backend/build/libs/backend-0.0.1-SNAPSHOT.jar
```

### Scheduled Ingestion

**Cron example** (daily at 2 AM):
```cron
0 2 * * * cd /path/to/project && ./gradlew :backend:ingest -Pusername=hikaru
```

**Kubernetes CronJob:**
```yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: chesslytics-ingest
spec:
  schedule: "0 2 * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: ingest
            image: chesslytics-backend:latest
            args:
            - java
            - -jar
            - /app/backend.jar
            - --spring.profiles.active=ingest
            - --username=hikaru
          restartPolicy: OnFailure
```

## Adding New Features

### Adding Lichess Support

1. Create `LichessClient` similar to `ChessComClient`
2. Create `LichessIngestionService`
3. Update `ChesslyticsIngestApplication` to support both sources

### Adding Analytics Endpoints

1. Add methods to repositories (e.g., `GameRepository.findByPlayer()`)
2. Create service layer for aggregations
3. Add endpoints in `GameController` or new `AnalyticsController`

## Tech Stack

- **Spring Boot 3.4.2** - Application framework
- **Spring Data JPA** - Data persistence
- **Spring Web** - REST API
- **Hibernate** - ORM
- **SQLite** (default) / **PostgreSQL** - Database
- **Jackson** - JSON processing
- **Spotless** - Code formatting
- **Checkstyle** - Code quality
