# Use a Java 21 base image
FROM openjdk:21-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the Maven POM file
COPY pom.xml .

# Download dependencies (this will cache dependencies if the pom.xml is not changed)
RUN mvn dependency:go-offline

# Copy the project files
COPY src /app/src

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller base image for running the app
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/musicproject-0.0.1-SNAPSHOT.jar /app/musicproject.jar

# Expose the port your Spring Boot app is running on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/musicproject.jar"]