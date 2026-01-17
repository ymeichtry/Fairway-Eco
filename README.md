# Fairway-Eco

Golf ball trading platform with microservices architecture.

## Architecture

- **Frontend**: React + TypeScript + Vite (Port 3000)
- **Gateway**: Spring Cloud Gateway (Port 8090) - API Gateway with Circuit Breaker
- **Backend**: Spring Boot + Kafka (Port 8080)
- **Eureka Server**: Service Discovery (Port 8761)
- **Database**: MySQL
- **Message Queue**: Apache Kafka
- **Monitoring**: Kafka UI

## Architecture Flow

```
Frontend (Port 3000) → Gateway (Port 8090) → Backend (Port 8080) → MySQL + Kafka
                              ↓ Service Discovery ↓
                          Eureka Server (Port 8761)
```

The Gateway acts as the single entry point for all API requests, providing:

- Request routing to backend services
- Circuit breaker pattern for fault tolerance
- Retry logic for failed requests
- CORS handling
- Centralized error handling

The Eureka Server provides:

- Service registration and discovery
- Dynamic service location
- Health monitoring of microservices
- Load balancing support

## Prerequisites

- Docker & Docker Compose
- JDK 17 (for local backend development)
- Node.js 18+ (for local frontend development)

## Quick Start (Docker Compose)

Eureka Server: http://localhost:8761

- Kafka UI: http://localhost:8082

**Note**: Access all API endpoints through the Gateway at `http://localhost:8090/api/*`

**Service Discovery**: Check registered services at http://localhost:8761

````

This will start:

- Frontend: http://localhost:3000
- Gateway: http://localhost:8090
- Backend API: http://localhost:8080
- Kafka UI: http://localhost:8082

**Note**: Access all API endpoints through the Gateway at `http://localhost:8090/api/*`

## Local Development Setup

### Backend

```bash
cd FairwayEcoBackend

# Set up MySQL (requires Docker)
docker run --name mysql-fairway \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=fairwayeco \
  -e MYSQL_USER=fairwayeco \
  -e MYSQL_PASSWORD=fairwayeco_secret \
  -p 3306:3306 \
  -d mysql:8.0

# Set up Kafka (requires Docker)
docker-compose -f ../docker-compose.yml up mysql kafka zookeeper

# Build and run
./mvnw clean spring-boot:run
````

Backend runs on **http://localhost:8080**

### Gateway

```bash
cd FairwayEcoGateway

# Build and run
./mvnw spring-boot:run
```

Eureka Server

```bash
cd FairwayEcoEureka

# Build and run
./mvnw spring-boot:run
```

Eureka Server runs on **http://localhost:8761**

###

Gateway runs on **http://localhost:8090**

### Frontend

```bash
cd FairwayEcoFrontend

# Install dependencies
npm install

# Create .env.local (if not present)
echo "VITE_API_URL=http://localhost:8090/api" > .env.local

# Development server
npm run dev
```

Frontend runs on **http://localhost:8080**

## API Integration

The frontend connects to the backend via the API client:

- **Endpoint**: `VITE_API_URL` (defaults to `http://localhost:8080/api`)
- **Services**: Located in `src/api/services.ts`
  - `golfBallService` - Golf ball CRUD operations
  - `customerService` - Customer management
  - `orderService` - Order management

## Environment Variables

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

````

### Eureka Server (`application.properties`)

```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
### Frontend (`.env.local`)

```env
VITE_API_URL=http://localhost:8090/api
````

### Gateway (`application.properties`)

```properties
server.port=8090
backend.service.url=http://localhost:8080
```

### Backend (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fairwayeco
spring.datasource.username=fairwayeco
spring.datasource.password=fairwayeco_secret
spring.kafka.bootstrap-servers=localhost:9092
```

## CI/CD

- **CI Workflow** (`.github/workflows/ci.yml`):

  - Backend: Maven build & tests
  - Frontend: Node build & lint
  - Integration tests with MySQL

- **Docker Deployment** (`.github/workflows/docker-deploy.yml`):
  - Builds and pushes Backend image to Docker Hub
  - Builds and pushes Frontend image to Docker Hub
  - Requires secrets: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`

## Project Structure

````Eureka/ # Eureka Service Discovery
│   ├── src/main/java/       # Java source code
│   ├── pom.xml              # Maven configuration
│   └── Dockerfile           # Eureka container image
├── FairwayEco
Fairway-Eco/
├── FairwayEcoGateway/        # Spring Cloud Gateway (API Gateway)
│   ├── src/main/java/       # Java source code
│   ├── pom.xml              # Maven configuration
│   └── Dockerfile           # Gateway container image
├── FairwayEcoBackend/       # Spring Boot backend
│   Eureka Server

```bash
cd FairwayEcoEureka
./mvnw test
````

### ├── src/main/java/ # Java source code

│ ├── pom.xml # Maven configuration
│ └── Dockerfile # Backend container image
├── FairwayEcoFrontend/ # React frontend
│ ├── src/ # React components & pages
│ ├── package.json # Node dependencies
│ ├── Dockerfile # Frontend container image
│ └── nginx.conf # Nginx reverse proxy
├── postman/ # Postman collections & environments
│ ├── Fairway-Eco.postman_collection.json
│ ├── Fairway-Eco-Local.postman_environment.json
│ ├── Fairway-Eco-Docker.postman_environment.json
│ └── README.md # Postman usage guide
├── docker-compose.yml # Full stack orchestration
└── .github/workflows/ # GitHub Actions CI/CD

````

## Testing

### Gateway

```bash
cd FairwayEcoGateway
./mvnw test
````

### Backend

```bash
cd FairwayEcoBackend
./mvnw test
```

**Test Coverage**:

- ✅ **89 unit tests** across all architectural layers
- ✅ Service Layer: 37 tests (GolfBall, Customer, Order services)
- ✅ Controller Layer: 27 tests (REST API endpoints with MockMvc)
- ✅ Repository Layer: 24 tests (JPA custom queries with H2)
- ✅ Integration Tests: Spring Boot context loading

**Testing Frameworks**:

- JUnit 5 (Jupiter) for test structure
- Mockito for mocking dependencies
- AssertJ for fluent assertions
- Spring Boot Test (@WebMvcTest, @DataJpaTest)
- H2 in-memory database for repository tests

### Eureka Server

```bash
cd FairwayEcoEureka
./mvnw test
```

### Frontend

```bash
cd FairwayEcoFrontend
npm run lint
```

## API Testing with Postman

The project includes comprehensive Postman collections for testing all API endpoints.

### Import Collections

1. Open Postman
2. Click **Import** → **Choose Files**
3. Select files from `postman/` directory:
   - `Fairway-Eco.postman_collection.json` (Main collection)
   - `Fairway-Eco-Local.postman_environment.json` (Local environment)
   - `Fairway-Eco-Docker.postman_environment.json` (Docker environment)

### Available Endpoints

**Golf Balls API** (10 endpoints):

- CRUD operations (Create, Read, Update, Delete)
- Filter by brand, condition, price range
- Stock management

**Customers API** (6 endpoints):

- Customer management
- Search by email
- Address handling

**Orders API** (7 endpoints):

- Order creation and management
- Status updates (PENDING → PAID → PROCESSING → SHIPPED → DELIVERED)
- Order cancellation
- Filter by customer and status

**Health & Monitoring** (4 endpoints):

- Service health checks
- Circuit breaker status
- Eureka dashboard

For detailed usage instructions, see [postman/README.md](postman/README.md)

## Troubleshooting

**Eureka connection errors**: Ensure Eureka Server is running on port 8761. Check service registration at http://localhost:8761

**Service not registered**: Wait 30 seconds after startup for services to register with Eureka. Check application logs for connection errors.

**CORS errors**: CORS is handled by the Gateway. Check `FairwayEcoGateway/src/main/java/bbw/ch/gateway/config/CorsConfig.java`

**Circuit Breaker**: The project implements a two-level Circuit Breaker pattern:

- **Gateway Level**: Returns fallback responses (HTTP 503) when backend services are unreachable
- **Service Level**: Each service method has @CircuitBreaker annotations with fallback methods
  - GolfBallService: Handles product catalog failures gracefully
  - CustomerService: Manages customer data access failures
  - OrderService: Protects order processing from cascading failures

Check Circuit Breaker status at: http://localhost:8080/actuator/health (look for `circuitBreakers` component)

## Features

- ✅ **Microservices Architecture** with Spring Cloud
- ✅ **API Gateway** with Spring Cloud Gateway
- ✅ **Service Discovery** with Netflix Eureka
- ✅ **Circuit Breaker Pattern** with Resilience4j (Gateway + Service Level)
- ✅ **Event-Driven** with Apache Kafka
- ✅ **Docker Compose** for full-stack deployment
- ✅ **Production-ready CORS** configuration
- ✅ **Health Checks** for all services
- ✅ **Comprehensive Unit Tests** (89 tests across all layers)
- ✅ **Postman Collections** for API testing

- Gateway: Change `server.port` in `FairwayEcoGateway/src/main/resources/application.properties`
- Backend: Change `server.port` in `FairwayEcoBackend/src/main/resources/application.properties`
- Frontend: Change `server.port` in `FairwayEcoFrontend/vite.config.ts`

**Database connection**: Ensure MySQL is running and credentials match `application.properties`

**Gateway connection errors**: Verify backend service URL in Gateway's `application.properties`

**CORS errors**: CORS is handled by the Gateway. Check `FairwayEcoGateway/src/main/java/bbw/ch/gateway/config/CorsConfig.java`

**Circuit Breaker**: If services are unavailable, the Gateway returns fallback responses with HTTP 503
