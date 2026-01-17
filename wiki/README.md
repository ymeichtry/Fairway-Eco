# Fairway-Eco Wiki - Projektdokumentation

Willkommen zur Projektdokumentation von Fairway-Eco, einer Microservices-basierten E-Commerce-Plattform für den Handel mit gebrauchten Golfbällen.

## 📚 Dokumentationsübersicht

Diese Wiki-Dokumentation ist strukturiert gemäß den Aufgabenstellungen und demonstriert die erforderlichen Kompetenzen:

### Hauptdokumente

1. **[Projektdefinition & Architektur](01-Projektdefinition.md)**

   - Architekturskizze mit Mermaid-Diagrammen
   - Projektbeschreibung und Workflow
   - Technologie-Stack Übersicht
   - **Aufgabe 1** ✓

2. **[Architektur-Details](02-Architektur-Details.md)**

   - Detaillierte Microservices-Architektur
   - API Gateway Pattern
   - Service Discovery mit Eureka
   - Event-Driven Architecture mit Kafka
   - Circuit Breaker Pattern

3. **[Kompetenznachweise](03-Kompetenznachweise.md)**

   - A1E: Verteilte Systeme
   - A2E: Systemintegration
   - B1E: Data Management
   - C1E: Systemkomponenten-Evaluation
   - D1E: Datenaustausch-Implementierung
   - D2E: Schnittstellenprotokolle
   - E1E: Authentisierung & Autorisierung
   - F1E: Monitoring & Überwachung

4. **[Wochendokumentation](04-Wochendokumentation.md)**

   - Kontinuierliche Projektfortschritte
   - Wöchentliche Updates
   - **Aufgabe 2b** ✓

6. **[Testing & Quality Assurance](06-Testing.md)**
   - Unit Tests (89 Tests)
   - Integration Tests
   - Postman Collections
   - Test Coverage

## 🎯 Projektkontext

**Projekt**: Fairway-Eco - Nachhaltige Golf-Ball-Shop
**Ziel**: Implementierung einer Microservices-Architektur
**Team**: Oliver, Yannis, Joel
**Zeitraum**: Januar 2026  
**Repository**: https://github.com/[username]/Fairway-Eco

## 🏗️ Technologie-Stack

- **Frontend**: React, TypeScript, Vite, TailwindCSS
- **Backend**: Spring Boot 3.3.6, Java 17
- **Gateway**: Spring Cloud Gateway
- **Service Discovery**: Netflix Eureka
- **Message Queue**: Apache Kafka
- **Datenbank**: MySQL 8.0
- **Testing**: JUnit 5, Mockito, Postman
- **Container**: Docker, Docker Compose
- **Monitoring**: Kafka UI, Actuator

## 📋 Erfüllte Aufgaben

- ✅ **Aufgabe 1**: Projektdefinition mit Architekturskizze und Workflow-Beschreibung
- ✅ **Aufgabe 2a**: Git-Repository mit Zugang für Lehrperson (BBWLC)
- ✅ **Aufgabe 2b**: Wöchentliche Wiki-Dokumentation
- ✅ **Aufgabe 3**: Regelmäßige Abgabe via Git-Repository

## 📖 Leseempfehlung

1. **Start hier**: [01-Projektdefinition.md](01-Projektdefinition.md) - Überblick über Architektur und Idee
2. **Technische Details**: [02-Architektur-Details.md](02-Architektur-Details.md) - Vertiefung der Implementierung
3. **Kompetenzen**: [03-Kompetenznachweise.md](03-Kompetenznachweise.md) - Nachweis aller geforderten Kompetenzen
4. **Fortschritt**: [04-Wochendokumentation.md](04-Wochendokumentation.md) - Projektentwicklung über die Zeit
5. **Testing**: [06-Testing.md](06-Testing.md) - Qualitätssicherung und Tests

## 🚀 Quick Start

```bash
# Repository klonen
git clone https://github.com/[username]/Fairway-Eco.git
cd Fairway-Eco

# Mit Docker Compose starten
docker-compose up -d

# Services sind verfügbar unter:
# Frontend: http://localhost:3000
# Gateway: http://localhost:8090
# Eureka: http://localhost:8761
# Kafka UI: http://localhost:8082
```

---

_Letzte Aktualisierung: Januar 2026_
