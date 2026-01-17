# Fairway Eco Gateway

API Gateway for Fairway Eco microservices architecture.

## Overview

The Gateway acts as a single entry point for all client requests, routing them to the appropriate backend microservices. It provides:

- **Request Routing**: Routes requests to backend services based on path patterns
- **Circuit Breaker**: Implements fault tolerance with Resilience4j
- **Load Balancing**: Distributes requests across service instances (when used with service discovery)
- **CORS Handling**: Manages cross-origin requests from the frontend
- **Retry Logic**: Automatically retries failed requests
- **Fallback Responses**: Returns graceful error messages when services are unavailable

## Architecture

```
Frontend → Gateway (Port 8090) → Backend Services (Port 8080)
```

### Routes

| Path                | Target Service | Methods                | Circuit Breaker |
| ------------------- | -------------- | ---------------------- | --------------- |
| `/api/golfballs/**` | Backend        | GET, POST, PUT, DELETE | ✅              |
| `/api/customers/**` | Backend        | GET, POST, PUT, DELETE | ✅              |
| `/api/orders/**`    | Backend        | GET, POST, PUT, DELETE | ✅              |
| `/actuator/health`  | Backend        | GET                    | ❌              |

## Configuration

### Environment Variables

| Variable              | Default               | Description         |
| --------------------- | --------------------- | ------------------- |
| `server.port`         | `8090`                | Gateway port        |
| `backend.service.url` | `http://backend:8080` | Backend service URL |

### Circuit Breaker Settings

- **Sliding Window Size**: 10 requests
- **Minimum Number of Calls**: 5 requests before calculation
- **Failure Rate Threshold**: 50%
- **Wait Duration in Open State**: 5 seconds
- **Permitted Calls in Half-Open State**: 3 requests

## Running Locally

### With Maven

```bash
cd FairwayEcoGateway
./mvnw spring-boot:run
```

Gateway runs on **http://localhost:8090**

### With Docker

```bash
docker build -t fairwayeco-gateway .
docker run -p 8090:8090 \
  -e backend.service.url=http://backend:8080 \
  fairwayeco-gateway
```

### With Docker Compose

```bash
# From project root
docker-compose up gateway
```

## Testing

```bash
./mvnw test
```

## Health Check

```bash
curl http://localhost:8090/actuator/health
```

## Endpoints

### Gateway Management

- `GET /actuator/health` - Health status
- `GET /actuator/gateway/routes` - List all configured routes
- `GET /actuator/circuitbreakers` - Circuit breaker status

### Example Requests

```bash
# Get all golf balls (via gateway)
curl http://localhost:8090/api/golfballs

# Create an order (via gateway)
curl -X POST http://localhost:8090/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1, "items": [...]}'
```

## Circuit Breaker Fallbacks

When a backend service is unavailable, the gateway returns:

```json
{
  "message": "Service is temporarily unavailable. Please try again later.",
  "timestamp": "2025-01-16T10:30:00",
  "data": []
}
```

HTTP Status: `503 Service Unavailable`

## Development

### Project Structure

```
FairwayEcoGateway/
├── src/
│   ├── main/
│   │   ├── java/bbw/ch/gateway/
│   │   │   ├── FairwayEcoGatewayApplication.java
│   │   │   ├── config/
│   │   │   │   ├── GatewayConfig.java
│   │   │   │   └── CorsConfig.java
│   │   │   └── controller/
│   │   │       └── FallbackController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/bbw/ch/gateway/
├── Dockerfile
└── pom.xml
```

## Technologies

- **Spring Cloud Gateway** - Reactive gateway framework
- **Resilience4j** - Circuit breaker implementation
- **Spring Boot Actuator** - Health checks and metrics
- **Java 17** - Programming language
- **Maven** - Build tool
