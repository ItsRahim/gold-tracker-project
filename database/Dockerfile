FROM openjdk:17

LABEL authors="Rahim Ahmed"

ENV SPRING_PROFILES_ACTIVE=qa

WORKDIR /app

COPY src/main/resources/db/base/ /app/database

COPY target/database-0.0.1-SNAPSHOT.jar /app/database.jar

ENTRYPOINT ["java", "-jar", "/app/database.jar"]

CMD ["-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
