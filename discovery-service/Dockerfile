FROM openjdk:17-jdk-alpine

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=qa

WORKDIR /app

COPY target/discovery-service-0.0.1-SNAPSHOT.jar /discovery-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/discovery-service.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]