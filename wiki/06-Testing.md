# Testing & Quality Assurance

## 📊 Übersicht

| Tests | Coverage | Zeit |
|-------|----------|------|
| 89 (100% ✅) | 90% | 5.5s |

**Frameworks**: JUnit 5, Mockito, AssertJ, Spring Boot Test, H2

## 🧪 Test-Aufteilung

| Layer | Tests | Coverage | Tool |
|-------|-------|----------|------|
| Service | 37 | 95% | Mockito |
| Controller | 27 | 92% | @WebMvcTest |
| Repository | 24 | 88% | @DataJpaTest + H2 |

## 📮 API Tests

**Postman Collection**: 30+ Requests (Golf Balls, Customers, Orders, Health)

## 🚀 Ausführen

```bash
mvn test                    # Alle Tests
mvn test jacoco:report      # Mit Coverage
```

**Ergebnis**: Tests run: 89, Failures: 0, Errors: 0 ✅

---

*Letzte Aktualisierung*: 17.01.2026
