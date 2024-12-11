# First stage: Build the JAR file
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and project files
COPY pom.xml ./
COPY src ./src/
COPY mvnw ./
COPY .mvn ./.mvn

RUN chmod +x ./mvnw

# Build the application (JAR file)
RUN ./mvnw clean package -DskipTests

# Second stage: Run the application
FROM openjdk:17-jdk
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
