FROM openjdk:17-jdk-alpine

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=native,docker

WORKDIR /app

COPY src/main/resources/docker /app/opt/configs

COPY target/config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app/config-server.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]