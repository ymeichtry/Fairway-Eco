# Fairway-Eco ⛳

Golf Ball Trading Platform mit Microservices-Architektur.

## 🚀 Quick Start

```bash
docker-compose up -d
```

- Frontend: http://localhost:3000
- Gateway: http://localhost:8090
- Eureka: http://localhost:8761

## 📚 Dokumentation

➡️ **[Komplette Dokumentation im Wiki](./wiki/)**

- [Architektur & Mermaid-Diagramme](./wiki/01-Projektdefinition.md)
- [Kompetenznachweise A1E-F1E](./wiki/03-Kompetenznachweise.md)
- [Testing: 89 Tests, 90% Coverage](./wiki/06-Testing.md)
- [Postman Collections](./postman/README.md)

## 🧪 Tests ausführen

```bash
cd FairwayEcoBackend && ./mvnw test
```

---

**Stack**: Spring Boot 3.3.6 • Spring Cloud Gateway • Netflix Eureka • React 18 • Kafka • MySQL 8.0
