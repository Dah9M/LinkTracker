FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY . /app
RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/scrapper-0.0.1-SNAPSHOT.jar"]
