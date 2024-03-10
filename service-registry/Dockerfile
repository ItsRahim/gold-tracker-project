FROM openjdk:17
LABEL authors="rahim"
VOLUME /tmp
COPY /target/service-registry-0.0.1-SNAPSHOT.jar /service-registry.jar
ENTRYPOINT ["java", "-jar", "/service-registry.jar"]