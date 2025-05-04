FROM maven:3.8.6-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests
ARG TELEGRAM_BOT_TOKEN
ENV TELEGRAM_BOT_TOKEN=$TELEGRAM_BOT_TOKEN
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/seu-bot.jar .
CMD ["java", "-jar", "seu-bot.jar"]
