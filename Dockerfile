FROM gradle:8.10-jdk-21-and-23-alpine AS build

WORKDIR /app
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts /app/
COPY gradle /app/gradle

RUN ./gradlew dependencies --no-daemon

COPY src /app/src

RUN ./gradlew clean build -x test --no-daemon

FROM openjdk:21-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=production", "app.jar"]
