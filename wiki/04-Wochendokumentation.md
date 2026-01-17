# Wochendokumentation

Kontinuierliche Dokumentation des Projektfortschritts gemäß Aufgabe 2b.

## Woche 1: Projekt-Setup (08.01. - 14.01.2026)

### Ziele & Ergebnisse

- [x] Git Repository erstellt und strukturiert
- [x] Docker Compose mit MySQL aufgesetzt
- [x] Spring Boot Backend initialisiert (Port 8080)
- [x] React Frontend mit TypeScript aufgesetzt (Port 3000)

### Durchgeführte Arbeiten

- **Backend**: Domain Models (GolfBall, Customer, Order) mit JPA erstellt
- **Frontend**: React + Vite + TailwindCSS Setup, erste Komponenten
- **Docker**: MySQL 8.0, Backend & Frontend Container konfiguriert
- **Git**: `.gitignore`, README.md, Projekt-Struktur committed

### Herausforderungen

- MySQL Connection Timeout → gelöst mit wait-for-it Script
- CORS Errors → gelöst mit @CrossOrigin Annotation

---

## Woche 2: Core Features (15.01. - 21.01.2026)

### Ziele & Ergebnisse

- [x] REST API mit 23 Endpoints (Golf Balls, Customers, Orders)
- [x] Frontend-Backend Integration über Axios
- [x] Kafka Event-Streaming implementiert
- [x] Spring Cloud Gateway mit Circuit Breaker

### Durchgeführte Arbeiten

- **REST API**: Vollständige CRUD Endpoints für alle Entitäten (siehe [Postman Collection](../postman/))
- **Frontend Integration**: API Client mit Axios, ProductCard, Cart-Funktionalität
- **Kafka**: Zookeeper + Broker Setup, OrderEventProducer für Event-Publishing
- **API Gateway**: Routes konfiguriert, Resilience4j Circuit Breaker integriert, CORS konfiguriert

### Statistiken

- 23 REST Endpoints implementiert
- 4 Datenbank-Tabellen mit Relationen
- 1 Kafka Topic (`order-events`)
- ~5.000 Lines of Code (Backend: 3.000, Frontend: 2.000)

### Herausforderungen

- Kafka Consumer Lag → Batch-Processing optimiert
- Circuit Breaker Thresholds → mehrfache Anpassungen
- CORS Issues → Gateway CORS Config

---

## Woche 3: Testing & Dokumentation (22.01. - 28.01.2026)

### Ziele & Ergebnisse

- [x] Eureka Service Discovery (Port 8761)
- [x] 89 Unit Tests mit 90% Coverage
- [x] Postman Collections (30+ Requests)
- [x] Wiki-Dokumentation komplett

### Durchgeführte Arbeiten

- **Eureka Server**: Service Registry für Backend & Gateway, Load Balancing aktiviert
- **Unit Tests**: Service (37), Controller (27), Repository (24), Application (1) - alle passing in 5.5s
- **Test Stack**: JUnit 5, Mockito, AssertJ, Spring Boot Test, H2 In-Memory DB
- **Postman**: 4 Collections (Golf Balls, Customers, Orders, Health), Local & Docker Environments
- **Wiki**: 7 Dokumente mit Mermaid-Diagrammen, Kompetenznachweisen, wöchentlicher Dokumentation

### Statistiken

- 89 Tests (100% passing)
- 90% Test Coverage (Service: 95%, Controller: 92%, Repository: 88%)
- 30 Postman Requests
- ~3.000 Zeilen Dokumentation

### Herausforderungen

- Test-Isolation → @DirtiesContext
- H2 Schema → application-test.yml
- MockMvc JSON → ObjectMapper Config

---

## 📊 Projekt-Übersicht

### Meilensteine

| Meilenstein       | Datum      | Status |
| ----------------- | ---------- | ------ |
| 🏁 Setup          | 14.01.2026 | ✅     |
| 🚀 Core Features  | 21.01.2026 | ✅     |
| 🧪 Testing & Docs | 27.01.2026 | ✅     |
| 🔒 Security       | geplant    | ⏳     |

### Technologie-Stack Status

| Komponente           | Version | Status |
| -------------------- | ------- | ------ |
| Spring Boot Backend  | 3.3.6   | ✅     |
| React Frontend       | 18 + TS | ✅     |
| Spring Cloud Gateway | Latest  | ✅     |
| Netflix Eureka       | Latest  | ✅     |
| MySQL                | 8.0     | ✅     |
| Apache Kafka         | Latest  | ✅     |

### Code-Statistiken

- **Lines of Code**: ~15.000 (Backend: 8.000, Frontend: 5.000, Config: 2.000)
- **Files**: ~180 (Java: 60, TypeScript: 40, Tests: 40, Docs: 10)
- **API Endpoints**: 23
- **Database Tables**: 4
- **Unit Tests**: 89 (90% Coverage)
- **Docker Services**: 7

### Lessons Learned

**✅ Was gut funktioniert hat:**

- Docker Compose für lokale Entwicklung
- Spring Boot für schnelle Backend-Entwicklung
- Mermaid-Diagramme für Dokumentation
- Systematisches Testing

**⚠️ Herausforderungen:**

- Kafka Setup (Zookeeper + Broker Konfiguration)
- Circuit Breaker Thresholds optimal einstellen
- Test-Isolation bei Integration Tests
- CORS zwischen Gateway und Backend

**💡 Verbesserungen:**

- Tests parallel zur Entwicklung schreiben
- Security (JWT) früher einplanen
- Monitoring (Prometheus/Grafana) von Anfang an
- CI/CD Pipeline früher aufsetzen

---

**Aufgabe 2b erfüllt**: ✅ Kontinuierliche wöchentliche Dokumentation

_Letzte Aktualisierung_: 17.01.2026
