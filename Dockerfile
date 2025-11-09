# Build Stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle/ ./gradle/
COPY settings.gradle build.gradle ./

COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --build-cache --no-daemon

# Run Stage
FROM eclipse-temurin:21-jre AS run
WORKDIR /app

ARG JAR_FILE_PATH=app/build/libs/*.jar
ENV TZ=Asia/Seoul \
    USE_PROFILE=dev

RUN useradd -m -u 10001 queuing

COPY --from=build --chown=queuing:queuing ${JAR_FILE_PATH} app.jar

USER queuing:queuing

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "app.jar"]
