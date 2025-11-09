# Build Stage
FROM eclipse-temurin:21-jdk AS build

COPY gradlew gradle/ ./
COPY settings.gradle build.gradle ./
RUN chmod +x gradlew && ./gradlew --version

COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:21-jre AS run

ARG JAR_FILE_PATH=build/libs/*.jar
ENV TZ=Asia/Seoul \
    USE_PROFILE=dev

COPY --from=build ${JAR_FILE_PATH} app.jar

RUN useradd -m -u 10001 app && chown app:app app.jar
USER app

ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "app.jar"]
