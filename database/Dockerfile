# Build Stage
FROM maven:3.9.7-amazoncorretto-17 AS build

# Setting the current working directory for building the main project
WORKDIR /build

# Copy the pom.xml and download dependencies
COPY database/pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and package it into a jar
COPY database/src ./src
RUN mvn clean package -DskipTests

# Runtime Stage
FROM amazoncorretto:17

# Update the package repository
RUN yum update -y

# Set the working directory for the runtime stage
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /build/target/database-*.jar /app/database.jar

# Copy the resources separately
COPY database/src/main/resources/db/base /app/database

# Set label for authors
LABEL authors="Rahim Ahmed"