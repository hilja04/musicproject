# Maven Build Stage
FROM maven:3.8.7-openjdk-18-slim AS build

# Copy the entire project to the container
COPY . .

# Run Maven to package the application, skipping tests
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jdk-slim

# Copy the packaged JAR from the build stage
COPY --from=build /target/musicproject-0.0.1-SNAPSHOT.jar musicproject.jar

# Expose the port your Spring Boot app is running on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "musicproject.jar"]