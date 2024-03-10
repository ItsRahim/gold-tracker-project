FROM openjdk:17
LABEL authors="rahim"
VOLUME /tmp
COPY /target/config-server-0.0.1-SNAPSHOT.jar /config-server.jar
ENTRYPOINT ["java", "-jar", "/config-server.jar"]
