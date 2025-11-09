# Build Stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle/ ./gradle/
COPY settings.gradle build.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:21-jre AS run
WORKDIR /app

ARG JAR_FILE_PATH=app/build/libs/*.jar
ENV TZ=Asia/Seoul \
    USE_PROFILE=dev

RUN useradd -m -u 10001 woker

COPY --from=build --chown=worker:worker ${JAR_FILE_PATH} app.jar

USER worker:worker

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "app.jar"]
