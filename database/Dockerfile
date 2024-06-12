# Build Stage
FROM maven:3.9.7-amazoncorretto-17 AS build

# Setting the current working directory for building the main project
WORKDIR /build

# Installing all maven dependencies for pom.xml
COPY ../database-service/pom.xml .
RUN mvn dependency:go-offline

# Packaging source code into jar
COPY ../database-service/src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM amazoncorretto:17

RUN yum update -y

WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY src/main/resources/db/base/ /app/database
COPY --from=build /build/target/database-service-*.jar /app/database-service.jar

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=qa

ENTRYPOINT ["java", "-jar", "/app/database-service.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
