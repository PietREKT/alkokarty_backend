# Use the appropriate Java version for your project
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY pom.xml .
COPY src src

COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw
RUN ./mvnw cleadn package -DskipTests

FROM openjdk:17-jdk
VOLUME /tmp
# Set the argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
