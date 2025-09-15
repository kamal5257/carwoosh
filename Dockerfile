# Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
ADD /target/CarWoosh-0.0.1-SNAPSHOT.jar carwoosh.jar

# Set the entrypoint to run the JAR file
ENTRYPOINT ["java", "-jar", "carwoosh.jar"]

# Expose the port your application runs on (optional, depending on your app)
EXPOSE 8081
