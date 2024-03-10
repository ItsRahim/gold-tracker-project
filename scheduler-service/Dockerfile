FROM openjdk:17
LABEL authors="rahim"
VOLUME /tmp
COPY /target/scheduler-service-0.0.1-SNAPSHOT.jar /scheduler-service.jar
ENTRYPOINT ["java", "-jar", "/scheduler-service.jar"]
