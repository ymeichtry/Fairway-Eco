# Fairway-Eco Backend

## Übersicht

Fairway-Eco ist ein Online-Shop für recycelte Golfbälle, die aus Wasserhindernissen geborgen wurden. Das Backend ist als Microservice-Architektur mit Spring Boot entwickelt.

## Architektur

```
┌─────────────────────────────────────────────────────────────┐
│                    Fairway-Eco Backend                       │
├─────────────────────────────────────────────────────────────┤
│  Controllers (REST API)                                      │
│  ├── GolfBallController  - /api/v1/golf-balls               │
│  ├── CustomerController  - /api/v1/customers                │
│  └── OrderController     - /api/v1/orders                   │
├─────────────────────────────────────────────────────────────┤
│  Services (Business Logic)                                   │
│  ├── GolfBallService     - Produktverwaltung                │
│  ├── CustomerService     - Kundenverwaltung                 │
│  └── OrderService        - Bestellungsverwaltung            │
├─────────────────────────────────────────────────────────────┤
│  Events (Kafka Microservices)                               │
│  ├── OrderCreatedEvent                                       │
│  ├── OrderStatusChangedEvent                                 │
│  └── OrderCancelledEvent                                     │
├─────────────────────────────────────────────────────────────┤
│  Repositories (Data Access)                                  │
│  ├── GolfBallRepository                                      │
│  ├── CustomerRepository                                      │
│  └── OrderRepository                                         │
├─────────────────────────────────────────────────────────────┤
│  Infrastructure                                              │
│  ├── MySQL Database                                          │
│  ├── Apache Kafka                                            │
│  └── Resilience4j Circuit Breaker                           │
└─────────────────────────────────────────────────────────────┘
```

## Technologie-Stack

- **Java 17+** - Programmiersprache
- **Spring Boot 3.3.6** - Framework
- **Spring Data JPA** - Datenzugriff
- **Spring Kafka** - Microservice-Kommunikation
- **MySQL 8.0** - Datenbank
- **Resilience4j** - Circuit Breaker Pattern
- **Lombok** - Code-Generierung
- **Docker** - Containerisierung

## Projektstruktur

```
FairwayEcoBackend/
├── src/main/java/bbw/ch/FairwayEcoBackend/
│   ├── config/           # Konfigurationsklassen
│   ├── controller/       # REST Controller
│   ├── dto/              # Data Transfer Objects
│   ├── event/            # Kafka Events
│   ├── exception/        # Exception Handling
│   ├── mapper/           # Entity-DTO Mapper
│   ├── model/            # JPA Entities
│   ├── repository/       # Data Repositories
│   └── service/          # Business Logic
└── src/main/resources/
    └── application.properties
```

## Schnellstart

### Voraussetzungen

- Java 17 oder höher (getestet mit Java 17-21)
- Docker & Docker Compose
- Maven (oder nutze den mitgelieferten Maven Wrapper `./mvnw`)

### 1. Infrastruktur starten

```bash
# MySQL, Kafka und Zookeeper starten
docker-compose up -d
```

### 2. Anwendung starten

```bash
cd FairwayEcoBackend
./mvnw spring-boot:run
```

### 3. API testen

```bash
# Alle Golfbälle abrufen
curl http://localhost:8080/api/v1/golf-balls

# Neuen Golfball erstellen
curl -X POST http://localhost:8080/api/v1/golf-balls \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Titleist",
    "model": "Pro V1",
    "price": 2.50,
    "quantity": 100,
    "condition": "GRADE_A",
    "description": "Premium recycled golf ball"
  }'
```

## API Endpoints

### Golf Balls

| Method | Endpoint                                   | Beschreibung             |
| ------ | ------------------------------------------ | ------------------------ |
| GET    | `/api/v1/golf-balls`                       | Alle Golfbälle           |
| GET    | `/api/v1/golf-balls/{id}`                  | Golfball nach ID         |
| GET    | `/api/v1/golf-balls/available`             | Verfügbare Golfbälle     |
| GET    | `/api/v1/golf-balls/brand/{brand}`         | Nach Marke filtern       |
| GET    | `/api/v1/golf-balls/condition/{condition}` | Nach Zustand filtern     |
| GET    | `/api/v1/golf-balls/filter`                | Mit mehreren Filtern     |
| GET    | `/api/v1/golf-balls/brands`                | Alle Marken              |
| POST   | `/api/v1/golf-balls`                       | Neuen Golfball erstellen |
| PUT    | `/api/v1/golf-balls/{id}`                  | Golfball aktualisieren   |
| DELETE | `/api/v1/golf-balls/{id}`                  | Golfball löschen         |
| PATCH  | `/api/v1/golf-balls/{id}/stock`            | Lagerbestand ändern      |

### Customers

| Method | Endpoint                          | Beschreibung           |
| ------ | --------------------------------- | ---------------------- |
| GET    | `/api/v1/customers`               | Alle Kunden            |
| GET    | `/api/v1/customers/{id}`          | Kunde nach ID          |
| GET    | `/api/v1/customers/email/{email}` | Kunde nach E-Mail      |
| POST   | `/api/v1/customers`               | Neuen Kunden erstellen |
| PUT    | `/api/v1/customers/{id}`          | Kunden aktualisieren   |
| DELETE | `/api/v1/customers/{id}`          | Kunden löschen         |

### Orders

| Method | Endpoint                               | Beschreibung              |
| ------ | -------------------------------------- | ------------------------- |
| GET    | `/api/v1/orders`                       | Alle Bestellungen         |
| GET    | `/api/v1/orders/{id}`                  | Bestellung nach ID        |
| GET    | `/api/v1/orders/customer/{customerId}` | Bestellungen eines Kunden |
| GET    | `/api/v1/orders/status/{status}`       | Bestellungen nach Status  |
| POST   | `/api/v1/orders`                       | Neue Bestellung erstellen |
| PATCH  | `/api/v1/orders/{id}/status`           | Bestellstatus ändern      |
| POST   | `/api/v1/orders/{id}/cancel`           | Bestellung stornieren     |

## Microservices (Kafka Events)

Das System nutzt Apache Kafka für die asynchrone Kommunikation zwischen Microservices:

### Events

1. **OrderCreatedEvent** - Wird bei neuer Bestellung gesendet
2. **OrderStatusChangedEvent** - Wird bei Statusänderung gesendet
3. **OrderCancelledEvent** - Wird bei Stornierung gesendet

### Topics

- `order-created`
- `order-status-changed`
- `order-cancelled`
- `stock-updated`

## Ball-Zustände

| Zustand  | Beschreibung                                   |
| -------- | ---------------------------------------------- |
| MINT     | Wie neu, keine sichtbaren Markierungen         |
| GRADE_A  | Minimaler Verschleiss, ausgezeichneter Zustand |
| GRADE_B  | Leichte Kratzer, guter Zustand                 |
| GRADE_C  | Sichtbarer Verschleiss, Übungsqualität         |
| PRACTICE | Starker Verschleiss, Driving Range Qualität    |

## Order-Status

| Status     | Beschreibung                               |
| ---------- | ------------------------------------------ |
| PENDING    | Bestellung eingegangen, wartet auf Zahlung |
| PAID       | Zahlung bestätigt                          |
| PROCESSING | Bestellung wird bearbeitet                 |
| SHIPPED    | Bestellung wurde versendet                 |
| DELIVERED  | Bestellung wurde zugestellt                |
| CANCELLED  | Bestellung wurde storniert                 |
| REFUNDED   | Bestellung wurde erstattet                 |

## Monitoring

- **Health Check**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Info**: `http://localhost:8080/actuator/info`
- **Kafka UI**: `http://localhost:8081` (wenn Docker läuft)

## Nächste Schritte

- [ ] Authentifizierung mit Spring Security / JWT
- [ ] Frontend mit React/Vue.js
- [ ] Payment Integration (Stripe)
- [ ] E-Mail-Benachrichtigungen
- [ ] Admin Dashboard
- [ ] Rate Limiting
- [ ] API Versionierung
- [ ] OpenAPI/Swagger Dokumentation

## Umgebungsvariablen

| Variable                  | Beschreibung                 | Standardwert                             |
| ------------------------- | ---------------------------- | ---------------------------------------- |
| `DB_URL`                  | JDBC URL zur MySQL Datenbank | `jdbc:mysql://localhost:3306/fairwayeco` |
| `DB_USERNAME`             | Datenbank Benutzername       | `fairwayeco`                             |
| `DB_PASSWORD`             | Datenbank Passwort           | `fairwayeco_secret`                      |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka Broker Adresse         | `localhost:9092`                         |

## Entwicklung

### Projekt bauen

```bash
./mvnw clean package
```

### Tests ausführen

```bash
./mvnw test
```

### Docker Image erstellen

```bash
docker build -t fairwayeco-backend .
```

## Lizenz

Dieses Projekt wurde im Rahmen eines Schulprojekts (M321-M324) erstellt.
