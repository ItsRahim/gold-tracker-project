# Build Stage
FROM maven:3.9.7-amazoncorretto-17 AS build

# Set environment variable for the snapshot version
ARG SNAPSHOT_VERSION=0.0.1-SNAPSHOT
ENV SNAPSHOT_VERSION=${SNAPSHOT_VERSION}

# Building common jar
WORKDIR /common

# Copy the pom.xml file and source code for the common module
COPY ../common/pom.xml .
RUN mvn dependency:go-offline

COPY ../common/src ./src
RUN mvn clean package -DskipTests

# Install common jar into local repository
RUN mvn install:install-file -Dfile=target/common-${SNAPSHOT_VERSION}.jar -DgroupId=com.rahim -DartifactId=common -Dversion=${SNAPSHOT_VERSION} -Dpackaging=jar

# Setting the current working directory for building the main project
WORKDIR /build

# Installing all maven dependencies for pom.xml
COPY ../email-service/pom.xml .
RUN mvn dependency:go-offline

# Packaging source code into jar
COPY ../email-service/src ./src
RUN mvn clean package -DskipTests


# Runtime Stage
FROM amazoncorretto:17

RUN yum update -y

WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /build/target/email-service-*.jar /app/email-service.jar

EXPOSE 8083

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=qa

ENTRYPOINT ["java", "-jar", "/app/email-service.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]