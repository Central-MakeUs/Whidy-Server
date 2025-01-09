FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/study_map.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "app.jar"]
