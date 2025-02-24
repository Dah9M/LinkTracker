FROM maven:3.9.9-eclipse-temurin-23-alpine as builder
WORKDIR /app

COPY . /app

RUN mvn -pl bot -am clean package

FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/bot/target/bot-0.0.1-SNAPSHOT.jar /app/bot.jar

ENTRYPOINT ["java", "-jar", "/app/bot.jar"]
