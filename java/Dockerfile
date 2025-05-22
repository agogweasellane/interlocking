# 빌드 스테이지
FROM eclipse-temurin:17-alpine AS build
RUN apk add --no-cache bash

WORKDIR /app
RUN mkdir -p /app/logs
COPY src/main/resources/logback-spring.xml /app/config/logback-spring.xml

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY . .

RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests


# 런타임 스테이지
FROM eclipse-temurin:17-jre-alpine as runtime

WORKDIR /app
RUN mkdir -p /app/logs
COPY src/main/resources/logback-spring.xml /app/config/logback-spring.xml

RUN addgroup -g 1000 worker && \
    adduser -u 1000 -G worker -s /bin/sh -D worker && \
	chmod -R 755 /app/logs && \
	chown -R worker:worker /app/logs

COPY --from=build --chown=worker:worker /app/target/*.jar ./main.jar

USER worker:worker

ENV PROFILE=${PROFILE}

EXPOSE 80

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "main.jar", "--logging.config=/app/config/logback-spring.xml"]