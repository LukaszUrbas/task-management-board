# Task Management Board

Monorepo zawierające backend **Spring Boot** i frontend **Angular** do zarządzania zadaniami.

---

## Stos technologiczny

| Warstwa     | Technologia                          |
|-------------|--------------------------------------|
| Backend     | Java 20, Spring Boot 4.1, Spring Data JPA, Spring Data REST |
| Baza danych | PostgreSQL 17                        |
| Migracje    | Flyway                               |
| Frontend    | Angular 19, TypeScript, SCSS         |
| Serwer WWW  | nginx (produkcja)                    |
| Konteneryzacja | Docker, Docker Compose            |
| Build       | Maven 3 (multi-module), npm          |

---

## Struktura projektu

```
task-management-board/
├── backend/                  # Spring Boot API
│   ├── src/
│   │   ├── main/java/        # Kod aplikacji
│   │   │   └── com/app/taskmanagementboard/
│   │   │       ├── controller/   # REST controllers
│   │   │       ├── service/      # Logika biznesowa
│   │   │       ├── repository/   # Spring Data JPA
│   │   │       ├── entity/       # Encje JPA
│   │   │       ├── dto/          # Request/Response records
│   │   │       └── exception/    # Obsługa błędów
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/     # Migracje Flyway (V1__init.sql, ...)
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                 # Angular SPA
│   ├── src/app/
│   │   └── tasks/            # Moduł zadań (model, service, component)
│   ├── nginx.conf            # Konfiguracja nginx (proxy + SPA fallback)
│   ├── proxy.conf.json       # Proxy dla ng serve (dev)
│   ├── Dockerfile
│   └── pom.xml
├── compose.yaml              # Docker Compose (postgres + app + frontend)
└── pom.xml                   # Root aggregator Maven
```

---

## Wymagania

### Lokalne środowisko deweloperskie
- **Java 20+** (JDK)
- **Maven 3.9+** lub użyj dołączonego `./mvnw`
- **Node.js 22+** i **npm 10+**
- **Docker** i **Docker Compose**

### Weryfikacja
```bash
java -version
mvn -version           # lub: cd backend && ./mvnw -version
node --version
npm --version
docker --version
docker compose version
```

---

## Uruchamianie

### 🔧 Lokalny development (zalecane)

Trzy osobne terminale:

#### Terminal 1 — Baza danych (PostgreSQL w Dockerze)
```bash
docker compose up postgres
```
Baza będzie dostępna na `localhost:5432`.

#### Terminal 2 — Backend (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run
```
> Spring Boot automatycznie wykryje `compose.yaml` i uruchomi postgresa jeśli jeszcze nie działa.
> Flyway zastosuje migracje przy starcie.

API dostępne pod: `http://localhost:8080/api/tasks`

#### Terminal 3 — Frontend (Angular dev server z hot-reload)
```bash
cd frontend
npm install        # tylko przy pierwszym uruchomieniu
npm start          # ng serve z proxy na :8080
```
Aplikacja dostępna pod: `http://localhost:4200`

> Proxy w `proxy.conf.json` automatycznie przekierowuje żądania `/api/*` do backendu na porcie 8080.

---

### 🐳 Docker Compose (pełny stack)

Buduje i uruchamia wszystkie serwisy naraz:

```bash
docker compose up --build
```

| Serwis    | Adres                        |
|-----------|------------------------------|
| Frontend  | http://localhost             |
| Backend   | http://localhost:8080        |
| PostgreSQL| localhost:5432               |

Zatrzymanie:
```bash
docker compose down
```

Zatrzymanie z usunięciem danych (volume):
```bash
docker compose down -v
```

---

### 📦 Pełny build Maven

Buduje backend (JAR) i frontend (Angular dist) bez uruchamiania:

```bash
# Z katalogu głównego
mvn install -DskipTests

# Lub z Maven Wrapperem z katalogu backend
cd backend && ./mvnw install -DskipTests
```

---

## Migracje bazy danych (Flyway)

Migracje są stosowane **automatycznie przy starcie aplikacji**.

Pliki migracji znajdują się w:
```
backend/src/main/resources/db/migration/
```

### Konwencja nazewnictwa
```
V1__init.sql
V2__add_task_priority.sql
V3__add_users_table.sql
```
- Prefix `V` + numer + dwa podkreślniki + opis + `.sql`
- **Nigdy nie modyfikuj** już zastosowanego pliku (Flyway weryfikuje checksum)

### Ręczne operacje Flyway (wymaga działającej bazy)
```bash
cd backend

# Sprawdź stan migracji
./mvnw flyway:info

# Zastosuj oczekujące migracje
./mvnw flyway:migrate

# Wyczyść całą bazę (UWAGA: niszczy dane!)
./mvnw flyway:clean
```

---

## REST API

Base URL: `http://localhost:8080/api`

| Metoda   | Endpoint          | Opis                          |
|----------|-------------------|-------------------------------|
| `GET`    | `/tasks`          | Lista wszystkich zadań        |
| `GET`    | `/tasks?status=TODO` | Filtrowanie po statusie    |
| `GET`    | `/tasks/{id}`     | Jedno zadanie                 |
| `POST`   | `/tasks`          | Utwórz zadanie                |
| `PUT`    | `/tasks/{id}`     | Zaktualizuj zadanie           |
| `DELETE` | `/tasks/{id}`     | Usuń zadanie                  |

### Przykład — tworzenie zadania
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Pierwsze zadanie",
    "description": "Opis zadania",
    "status": "TODO"
  }'
```

### Dostępne statusy
| Wartość      | Opis        |
|--------------|-------------|
| `TODO`       | Do zrobienia |
| `IN_PROGRESS`| W trakcie   |
| `DONE`       | Gotowe      |

---

## Zmienne środowiskowe (backend)

| Zmienna               | Domyślna wartość       | Opis                   |
|-----------------------|------------------------|------------------------|
| `DB_HOST`             | `localhost`            | Host bazy danych       |
| `DB_PORT`             | `5432`                 | Port bazy danych       |
| `DB_NAME`             | `task_management_board`| Nazwa bazy             |
| `DB_USER`             | `task_management_board`| Użytkownik bazy        |
| `DB_PASSWORD`         | `task_management_board`| Hasło bazy             |
| `DOCKER_COMPOSE_ENABLED` | `true`             | Auto-start compose (dev)|

---

## Git — przydatne komendy

Alias do commitowania bez ręcznego `git add`:
```bash
git ca -m "opis zmian"   # automatycznie robi git add -A + commit
```

