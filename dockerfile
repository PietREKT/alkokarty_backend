# Use the appropriate Java version for your project
FROM openjdk:17-jdk-alpine

# Set the argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
