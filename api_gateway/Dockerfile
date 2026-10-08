FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar
COPY src/main/resources/application.yml application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]