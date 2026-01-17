# Circuit Breaker Pattern Implementation

## Overview

This document describes the complete Circuit Breaker implementation in the Fairway-Eco project using Resilience4j.

## Architecture

The project implements a **two-level Circuit Breaker pattern**:

1. **Gateway Level**: Circuit breakers in the Spring Cloud Gateway
2. **Service Level**: Circuit breakers on individual service methods

### 1. Gateway Level Circuit Breaker

**Location**: `FairwayEcoGateway/src/main/java/bbw/ch/gateway/config/GatewayConfig.java`

**Purpose**: Protects the gateway from cascading failures when backend services are down

**Configuration**:

```java
.filters(f -> f
    .circuitBreaker(c -> c
        .setName("golfballCircuitBreaker")
        .setFallbackUri("forward:/fallback"))
    .retry(r -> r.setRetries(3))
)
```

**Behavior**:

- If backend service fails, gateway returns HTTP 503 with fallback response
- Automatically retries failed requests up to 3 times
- Prevents overwhelming failed services with continuous requests

### 2. Service Level Circuit Breaker

**Location**: Service implementations in `FairwayEcoBackend/src/main/java/bbw/ch/FairwayEcoBackend/service/`

**Purpose**: Provides fine-grained failure handling at the business logic level

#### Implemented Services:

1. **GolfBallServiceImpl** (`golfballService` circuit breaker)

   - Methods protected: `createGolfBall`, `getGolfBallById`, `getAllGolfBalls`, `getAvailableGolfBalls`, `getGolfBallsByBrand`, `getGolfBallsByCondition`, `getGolfBallsByFilters`, `getAllBrands`, `updateGolfBall`, `deleteGolfBall`, `updateStock`
   - Fallback behavior: Returns empty lists for read operations, throws service unavailable for write operations

2. **CustomerServiceImpl** (`customerService` circuit breaker)

   - Methods protected: `createCustomer`, `getCustomerById`, `getCustomerByEmail`, `getAllCustomers`, `updateCustomer`, `deleteCustomer`
   - Fallback behavior: Returns empty lists for read operations, throws service unavailable for write operations

3. **OrderServiceImpl** (`orderService` circuit breaker)
   - Methods protected: `createOrder`, `getOrderById`, `getAllOrders`, `getOrdersByCustomerId`, `getOrdersByStatus`, `updateOrderStatus`, `cancelOrder`
   - Fallback behavior: Returns empty lists for read operations, throws service unavailable for write operations

#### Implementation Pattern:

```java
@CircuitBreaker(name = "golfballService", fallbackMethod = "getAllGolfBallsFallback")
public List<GolfBallResponseDto> getAllGolfBalls() {
    return golfBallRepository.findAll().stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
}

private List<GolfBallResponseDto> getAllGolfBallsFallback(Exception ex) {
    log.error("Circuit breaker fallback: Failed to get all golf balls", ex);
    return new ArrayList<>();
}
```

## Configuration

**Location**: `FairwayEcoBackend/src/main/resources/application.properties`

### Default Configuration:

```properties
resilience4j.circuitbreaker.instances.default.register-health-indicator=true
resilience4j.circuitbreaker.instances.default.sliding-window-size=10
resilience4j.circuitbreaker.instances.default.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.default.permitted-number-of-calls-in-half-open-state=3
resilience4j.circuitbreaker.instances.default.automatic-transition-from-open-to-half-open-enabled=true
resilience4j.circuitbreaker.instances.default.wait-duration-in-open-state=5s
resilience4j.circuitbreaker.instances.default.failure-rate-threshold=50
```

### Service-Specific Configurations:

Each service has its own configuration:

```properties
# GolfBall Service
resilience4j.circuitbreaker.instances.golfballService.register-health-indicator=true
resilience4j.circuitbreaker.instances.golfballService.sliding-window-size=10
resilience4j.circuitbreaker.instances.golfballService.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.golfballService.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.golfballService.wait-duration-in-open-state=10s

# Customer Service
resilience4j.circuitbreaker.instances.customerService.register-health-indicator=true
resilience4j.circuitbreaker.instances.customerService.sliding-window-size=10
resilience4j.circuitbreaker.instances.customerService.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.customerService.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.customerService.wait-duration-in-open-state=10s

# Order Service
resilience4j.circuitbreaker.instances.orderService.register-health-indicator=true
resilience4j.circuitbreaker.instances.orderService.sliding-window-size=10
resilience4j.circuitbreaker.instances.orderService.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.orderService.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.orderService.wait-duration-in-open-state=10s
```

### Configuration Parameters Explained:

- **sliding-window-size**: Number of calls recorded when the circuit breaker is closed (10 calls)
- **minimum-number-of-calls**: Minimum number of calls before failure rate is calculated (5 calls)
- **failure-rate-threshold**: Percentage of failures to open the circuit (50%)
- **wait-duration-in-open-state**: Time to wait before transitioning to half-open (10 seconds)
- **permitted-number-of-calls-in-half-open-state**: Number of test calls in half-open state (3 calls)

## Circuit Breaker States

1. **CLOSED** (Normal Operation):

   - All requests pass through
   - Failures are recorded
   - When failure rate > 50% → transitions to OPEN

2. **OPEN** (Failure Mode):

   - Requests immediately fail and trigger fallback
   - No calls to the failing service
   - After 10 seconds → transitions to HALF_OPEN

3. **HALF_OPEN** (Recovery Testing):
   - Allows 3 test calls
   - If successful → transitions back to CLOSED
   - If failures continue → back to OPEN

## Monitoring

### Health Check Endpoint

Circuit Breaker status is exposed via Spring Actuator:

```bash
curl http://localhost:8080/actuator/health
```

**Response**:

```json
{
  "status": "UP",
  "components": {
    "circuitBreakers": {
      "status": "UP",
      "details": {
        "golfballService": {
          "status": "UP",
          "details": {
            "failureRate": "-1.0%",
            "state": "CLOSED",
            "bufferedCalls": 0,
            "failedCalls": 0
          }
        },
        "customerService": {
          "status": "UP",
          "details": {
            "state": "CLOSED"
          }
        },
        "orderService": {
          "status": "UP",
          "details": {
            "state": "CLOSED"
          }
        }
      }
    }
  }
}
```

### Viewing Circuit Breaker Metrics

```bash
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
```

## Dependencies

Required Maven dependencies (already included in `pom.xml`):

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Benefits

1. **Fault Isolation**: Failures in one service don't cascade to others
2. **Graceful Degradation**: System continues to function with reduced capability
3. **Automatic Recovery**: Circuit breaker automatically tests and recovers
4. **Better User Experience**: Fast failures instead of long timeouts
5. **System Stability**: Prevents overwhelming failed services
6. **Observability**: Circuit breaker states visible in health endpoint

## Testing the Circuit Breaker

### Simulating Failure

1. Stop the MySQL container:

   ```bash
   docker stop fairwayeco-mysql
   ```

2. Make API calls - they should fail and trigger circuit breaker:

   ```bash
   curl http://localhost:8080/api/v1/golf-balls/brands
   ```

3. Check circuit breaker state:

   ```bash
   curl http://localhost:8080/actuator/health | python3 -m json.tool
   ```

4. You should see `"state": "OPEN"` for the affected circuit breakers

5. Restart MySQL and wait 10 seconds - circuit breaker will transition to HALF_OPEN then CLOSED

## Best Practices

1. **Fallback Methods**: Always provide meaningful fallback responses
2. **Read vs Write**: Use different fallback strategies for read (return empty) vs write (throw error) operations
3. **Logging**: Log all circuit breaker fallback invocations for debugging
4. **Configuration**: Tune thresholds based on actual traffic patterns
5. **Monitoring**: Regularly check circuit breaker metrics in production

## Future Enhancements

- [ ] Rate Limiting with Resilience4j
- [ ] Bulkhead Pattern for thread isolation
- [ ] Time Limiter for timeout handling
- [ ] Retry Pattern with exponential backoff
- [ ] Metrics export to Prometheus/Grafana
