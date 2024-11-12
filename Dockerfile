# Use a Maven image with Java 21 for the build stage
FROM maven:3.8.8-eclipse-temurin-21 AS build

# Copy the entire project to the container
COPY . .

# Run Maven to package the application, skipping tests
RUN mvn clean package -DskipTests

# Use Java 21 for the runtime stage
FROM eclipse-temurin:21-jre

# Copy the packaged JAR from the build stage
COPY --from=build /target/musicproject-0.0.1-SNAPSHOT.jar musicproject.jar

# Expose the port your Spring Boot app is running on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "musicproject.jar"]