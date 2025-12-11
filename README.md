# Fairway-Eco

Golf ball trading platform with microservices architecture.

## Architecture

- **Frontend**: React + TypeScript + Vite (Port 8080)
- **Backend**: Spring Boot + Kafka (Port 8080)
- **Database**: MySQL
- **Message Queue**: Apache Kafka
- **Monitoring**: Kafka UI

## Prerequisites

- Docker & Docker Compose
- JDK 17 (for local backend development)
- Node.js 18+ (for local frontend development)

## Quick Start (Docker Compose)

```bash
docker-compose up -d
```

This will start:
- Frontend: http://localhost:8080
- Backend API: http://localhost:8080/api
- Kafka UI: http://localhost:8082

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
```

Backend runs on **http://localhost:8080**

### Frontend

```bash
cd FairwayEcoFrontend

# Install dependencies
npm install

# Create .env.local (if not present)
echo "VITE_API_URL=http://localhost:8080/api" > .env.local

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

### Frontend (`.env.local`)

```env
VITE_API_URL=http://localhost:8081/api
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

```
Fairway-Eco/
├── FairwayEcoBackend/       # Spring Boot backend
│   ├── src/main/java/       # Java source code
│   ├── pom.xml              # Maven configuration
│   └── Dockerfile           # Backend container image
├── FairwayEcoFrontend/      # React frontend
│   ├── src/                 # React components & pages
│   ├── package.json         # Node dependencies
│   ├── Dockerfile           # Frontend container image
│   └── nginx.conf           # Nginx reverse proxy
├── docker-compose.yml       # Full stack orchestration
└── .github/workflows/       # GitHub Actions CI/CD
```

## Testing

### Backend
```bash
cd FairwayEcoBackend
./mvnw test
```

### Frontend
```bash
cd FairwayEcoFrontend
npm run lint
```

## Troubleshooting

**Port conflicts**: If port 8080/8081 are in use:
- Change frontend port in `FairwayEcoFrontend/vite.config.ts` → `server.port`
- Change backend port in `FairwayEcoBackend/src/main/resources/application.properties` → `server.port`

**Database connection**: Ensure MySQL is running and credentials match `application.properties`

**CORS errors**: Check `FairwayEcoBackend/src/main/java/bbw/ch/FairwayEcoBackend/config/CorsConfig.java` for allowed origins