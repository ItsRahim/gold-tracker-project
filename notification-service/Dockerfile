FROM openjdk:17-jdk-alpine

RUN apk update && apk upgrade && apk add curl

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=native,qa

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/notification-service.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]