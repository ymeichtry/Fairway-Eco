# Agent Instructions für Fairway-Eco Backend

## Projektübersicht

**Fairway-Eco** ist ein Online-Shop für recycelte Golfbälle, die aus Wasserhindernissen geborgen wurden. Das Backend ist eine Spring Boot Microservice-Architektur.

## Technologie-Stack

- **Java 17+**
- **Spring Boot 3.3.6**
- **Spring Data JPA** (MySQL)
- **Spring Kafka** (Microservice-Kommunikation)
- **Resilience4j** (Circuit Breaker)
- **Lombok**
- **Docker & Docker Compose**

## Projektstruktur

```
FairwayEcoBackend/
├── src/main/java/bbw/ch/FairwayEcoBackend/
│   ├── config/           # Kafka & App Konfiguration
│   ├── controller/       # REST Controller (GolfBall, Customer, Order)
│   ├── dto/              # Data Transfer Objects
│   ├── event/            # Kafka Events & Publisher
│   ├── exception/        # Custom Exceptions & GlobalExceptionHandler
│   ├── mapper/           # Entity-DTO Mapper
│   ├── model/            # JPA Entities (GolfBall, Customer, Order, OrderItem)
│   ├── repository/       # Spring Data Repositories
│   └── service/          # Business Logic Services
├── src/main/resources/
│   ├── application.properties
│   └── application.properties.example
├── docker-compose.yml    # MySQL, Kafka, Zookeeper
├── Dockerfile
└── pom.xml
```

## Wichtige Konventionen

### Code Style

- **Package-Struktur**: `bbw.ch.FairwayEcoBackend.<layer>`
- **Naming**: CamelCase für Klassen, camelCase für Methoden/Variablen
- **DTOs**: `*CreateDto` für Input, `*ResponseDto` für Output
- **Services**: Interface + `*Impl` Implementierung

### REST API Design

- **Base Path**: `/api/v1/`
- **Ressourcen**: Plural (z.B. `/golf-balls`, `/customers`, `/orders`)
- **HTTP Methods**: GET (lesen), POST (erstellen), PUT (aktualisieren), DELETE (löschen), PATCH (teilweise aktualisieren)
- **Response Codes**: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict

### Kafka Events

- **Topics**: `order-created`, `order-status-changed`, `order-cancelled`, `stock-updated`
- **Event Naming**: `*Event` Suffix (z.B. `OrderCreatedEvent`)
- **Publisher**: `OrderEventPublisher` in `event/` Package

## Domain Models

### GolfBall

Recycelte Golfbälle mit Zustandsbewertung:

- `MINT` - Wie neu
- `GRADE_A` - Ausgezeichnet
- `GRADE_B` - Gut
- `GRADE_C` - Übungsqualität
- `PRACTICE` - Driving Range

### Order Status Flow

```
PENDING → PAID → PROCESSING → SHIPPED → DELIVERED
    ↓       ↓         ↓
CANCELLED  CANCELLED  CANCELLED
                              ↓
                          REFUNDED
```

## Entwicklungs-Befehle

```bash
# Infrastruktur starten
docker-compose up -d

# Anwendung starten
./mvnw spring-boot:run

# Tests ausführen
./mvnw test

# Projekt bauen
./mvnw clean package

# Docker Image erstellen
docker build -t fairwayeco-backend .
```

## API Endpoints

| Ressource | Basis-Pfad           | Beschreibung      |
| --------- | -------------------- | ----------------- |
| GolfBalls | `/api/v1/golf-balls` | Produktverwaltung |
| Customers | `/api/v1/customers`  | Kundenverwaltung  |
| Orders    | `/api/v1/orders`     | Bestellverwaltung |

## Umgebungsvariablen

| Variable                  | Beschreibung       |
| ------------------------- | ------------------ |
| `DB_URL`                  | MySQL JDBC URL     |
| `DB_USERNAME`             | Datenbank User     |
| `DB_PASSWORD`             | Datenbank Passwort |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka Broker       |

## Hinweise für Änderungen

1. **Neue Entities**: Erstelle Model, Repository, DTO, Mapper, Service, Controller
2. **Neue Endpoints**: Füge sie zum entsprechenden Controller hinzu
3. **Neue Kafka Events**: Erstelle Event-Klasse und erweitere Publisher
4. **Exception Handling**: Nutze bestehende Exceptions oder erstelle neue in `exception/`
5. **Validierung**: Nutze Jakarta Validation Annotations (`@NotBlank`, `@Valid`, etc.)

## Tests

- Unit Tests in `src/test/java/`
- H2 In-Memory DB für Tests
- Spring Kafka Test für Event-Tests
