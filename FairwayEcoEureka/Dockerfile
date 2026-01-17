# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd -g 1001 appgroup && useradd -u 1001 -g appgroup appuser
COPY --from=build /app/target/eureka-server.jar app.jar
USER appuser
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
