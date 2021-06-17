FROM openjdk:8-jdk-alpine
RUN addgroup -S studio && adduser -S studio -G studio
USER studio:studio
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]