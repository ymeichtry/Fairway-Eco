# Fairway-Eco Postman Collections

This directory contains Postman collections and environments for testing the Fairway-Eco API.

## Files

- **Fairway-Eco.postman_collection.json** - Complete API collection with all endpoints
- **Fairway-Eco-Local.postman_environment.json** - Environment for local development
- **Fairway-Eco-Docker.postman_environment.json** - Environment for Docker deployment

## Import Instructions

1. Open Postman
2. Click **Import** button (top left)
3. Drag and drop all JSON files or click **Choose Files**
4. Select all files in this directory
5. Click **Import**

## Environment Setup

After importing, select the appropriate environment from the dropdown in the top right:

- **Fairway-Eco Local** - For testing against locally running services
- **Fairway-Eco Docker** - For testing against Docker Compose deployment

## Environment Variables

### Local Environment

- `base_url`: http://localhost:8080 (Backend API)
- `gateway_url`: http://localhost:8090 (API Gateway)
- `eureka_url`: http://localhost:8761 (Service Discovery)
- `frontend_url`: http://localhost:3000 (React Frontend)

### Docker Environment

- `base_url`: http://localhost:8080 (Backend API in Docker)
- `gateway_url`: http://localhost:8090 (Gateway in Docker)
- `eureka_url`: http://localhost:8761 (Eureka in Docker)
- `frontend_url`: http://localhost:3000 (Frontend in Docker)

## Collection Structure

### Golf Balls

- Get All Golf Balls
- Get Golf Ball by ID
- Create Golf Ball
- Update Golf Ball
- Delete Golf Ball
- Filter Golf Balls (by brand, condition, price range)
- Get Golf Balls by Brand
- Get Golf Balls by Condition
- Get All Brands
- Update Stock

### Customers

- Get All Customers
- Get Customer by ID
- Create Customer
- Update Customer
- Delete Customer
- Get Customer by Email

### Orders

- Get All Orders
- Get Order by ID
- Create Order
- Update Order Status
- Cancel Order
- Get Orders by Customer
- Get Orders by Status

### Health & Monitoring

- Health Check (Backend)
- Circuit Breaker Status
- Gateway Health
- Eureka Dashboard

## Usage Tips

### 1. Start with Health Checks

Before testing API endpoints, verify all services are running:

```
GET {{base_url}}/actuator/health
GET {{gateway_url}}/actuator/health
GET {{eureka_url}}
```

### 2. Create Test Data

Follow this order to create test data:

1. Create a Golf Ball (POST /api/v1/golfballs)
2. Create a Customer (POST /api/v1/customers)
3. Create an Order (POST /api/v1/orders)

### 3. Test Order Flow

Complete order lifecycle:

1. Create Order → Status: PENDING
2. Update Status to PAID → Status: PAID
3. Update Status to PROCESSING → Status: PROCESSING
4. Update Status to SHIPPED → Status: SHIPPED
5. Update Status to DELIVERED → Status: DELIVERED

Or cancel:

- Cancel Order (POST /api/v1/orders/{id}/cancel) → Status: CANCELLED

### 4. Test Circuit Breaker

To test the circuit breaker pattern:

1. Stop the backend service
2. Make API requests through the Gateway
3. Observe fallback responses (HTTP 503)
4. Restart backend and verify recovery

### 5. Test Filters

Golf Ball filtering examples:

```
GET {{base_url}}/api/v1/golfballs/filter?brand=Titleist&condition=MINT
GET {{base_url}}/api/v1/golfballs/filter?minPrice=20&maxPrice=35
GET {{base_url}}/api/v1/golfballs/filter?brand=Callaway&minPrice=15&maxPrice=30
```

## Order Status Values

Valid order status values for "Update Order Status":

- `PENDING` - Order created, awaiting payment
- `PAID` - Payment received
- `PROCESSING` - Order being prepared
- `SHIPPED` - Order shipped to customer
- `DELIVERED` - Order delivered
- `CANCELLED` - Order cancelled
- `REFUNDED` - Order refunded

## Golf Ball Condition Values

Valid condition values:

- `MINT` - Brand new or like new
- `GRADE_A` - Excellent condition
- `GRADE_B` - Good condition with minor marks
- `GRADE_C` - Acceptable condition
- `PRACTICE` - Practice grade

## Testing Through Gateway vs Direct

### Direct Backend Access

```
{{base_url}}/api/v1/golfballs
http://localhost:8080/api/v1/golfballs
```

### Through API Gateway (Recommended)

```
{{gateway_url}}/api/v1/golfballs
http://localhost:8090/api/v1/golfballs
```

The Gateway provides:

- Circuit breaker protection
- Request routing
- CORS handling
- Retry logic

## Troubleshooting

### Connection Refused

- Verify services are running: `docker ps` or check local processes
- Check ports are not blocked by firewall
- Ensure environment variables match your setup

### 404 Not Found

- Verify the endpoint URL is correct
- Check service is registered in Eureka: http://localhost:8761
- Wait 30 seconds after startup for service registration

### 500 Internal Server Error

- Check backend logs for details
- Verify database connection (MySQL on port 3306)
- Ensure Kafka is running (if testing order creation)

### Circuit Breaker Open

- This is expected when backend is unavailable
- Gateway returns HTTP 503 with fallback message
- Verify backend health at {{base_url}}/actuator/health

## Example Request Bodies

### Create Golf Ball

```json
{
  "brand": "Titleist",
  "model": "Pro V1",
  "condition": "MINT",
  "price": 29.99,
  "quantity": 100,
  "description": "Premium golf balls in mint condition",
  "imageUrl": "https://example.com/titleist-prov1.jpg"
}
```

### Create Customer

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "address": {
    "street": "123 Golf Lane",
    "city": "Golftown",
    "postalCode": "12345",
    "country": "USA"
  }
}
```

### Create Order

```json
{
  "customerId": 1,
  "items": [
    {
      "golfBallId": 1,
      "quantity": 2,
      "unitPrice": 29.99
    }
  ],
  "shippingAddress": {
    "street": "123 Golf Lane",
    "city": "Golftown",
    "postalCode": "12345",
    "country": "USA"
  }
}
```

## Automation & Testing

You can use Postman's Collection Runner to:

1. Run all requests in sequence
2. Test the complete workflow
3. Generate test reports
4. Integrate with CI/CD pipelines

## Support

For issues or questions:

1. Check service logs
2. Verify health endpoints
3. Review backend API documentation
4. Check Eureka dashboard for service status
