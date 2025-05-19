# Dockerfile for deploy the code on EC2 Instance
# Stage 1: Build the WAR file
FROM maven:3.8.4-openjdk-17-slim as builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Create a smaller image with just the WAR file
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the WAR file from the builder stage
COPY --from=builder /app/target/Rushr-0.0.1-SNAPSHOT.war /app/Rushr-0.0.1-SNAPSHOT.war

EXPOSE 8080

CMD ["java", "-jar", "/app/Rushr-0.0.1-SNAPSHOT.war"]
