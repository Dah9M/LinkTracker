FROM maven:3.9.9-eclipse-temurin-23-alpine as builder

WORKDIR /app

COPY . /app
RUN mvn -pl scrapper -am clean package

FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY --from=builder /app/scrapper/target/scrapper-0.0.1-SNAPSHOT.jar /app/scrapper.jar

ENTRYPOINT ["java", "-jar", "/app/scrapper.jar"]
