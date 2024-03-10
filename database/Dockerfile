FROM openjdk:17
LABEL authors="rahim"
VOLUME /tmp
COPY /target/database-0.0.1-SNAPSHOT.jar /database.jar
ENTRYPOINT ["java", "-jar", "/database.jar"]
