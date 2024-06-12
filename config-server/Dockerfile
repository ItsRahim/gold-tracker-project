# Build Stage
FROM maven:3.9.7-amazoncorretto-17 AS build

# Setting the current working directory for building the main project
WORKDIR /build

# Installing all maven dependencies for pom.xml
COPY ../config-server/pom.xml .
RUN mvn dependency:go-offline

# Packaging source code into jar
COPY ../config-server/src ./src
RUN mvn clean package -DskipTests


# Runtime Stage
FROM amazoncorretto:17

RUN yum update -y

WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /build/target/config-server-*.jar /app/config-server.jar
COPY ../config-server/src/main/resources/qa /app/opt/configs

EXPOSE 8888

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=native,qa

ENTRYPOINT ["java", "-jar", "/app/config-server.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]