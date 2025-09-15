# -------- Stage 1: Build the project --------
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration first (to leverage caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the project (skip tests for faster builds)
RUN mvn clean package -DskipTests

# -------- Stage 2: Run the app --------
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar ./carwoosh.jar

# Expose the port Render expects
ENV PORT 8089
EXPOSE 8089

# Command to run the app
ENTRYPOINT ["java", "-jar", "carwoosh.jar"]
