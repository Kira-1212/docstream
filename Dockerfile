# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests -B

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S docstream && adduser -S docstream -G docstream

COPY --from=builder /app/target/*.jar app.jar

RUN chown docstream:docstream app.jar

USER docstream

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]