# Build Stage
FROM maven:3.9.7-amazoncorretto-17 AS build

# Set environment variable for the snapshot version
ARG SNAPSHOT_VERSION=0.0.1-SNAPSHOT
ENV SNAPSHOT_VERSION=${SNAPSHOT_VERSION}

WORKDIR /build

# Installing all maven dependencies for pom.xml
COPY ../discovery-service/pom.xml .
RUN mvn dependency:go-offline

# Packaging source code into jar
COPY ../discovery-service/src ./src
RUN mvn clean package -DskipTests


# Runtime Stage
FROM amazoncorretto:17

RUN yum update -y

WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /build/target/discovery-service-*.jar /app/discovery-service.jar

EXPOSE 8761

LABEL authors="Rahim Ahmed"