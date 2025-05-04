# Estágio de construção
FROM maven:3.8.6-openjdk-11 AS builder

WORKDIR /app

# 1. Copia os arquivos de construção primeiro
COPY pom.xml .
COPY src ./src

# 2. Define a variável de ambiente ANTES do build
ARG TELEGRAM_BOT_TOKEN
ENV TELEGRAM_BOT_TOKEN=$TELEGRAM_BOT_TOKEN

# 3. Baixa dependências e faz o build (com cache otimizado)
RUN mvn dependency:resolve
RUN mvn clean package -DskipTests

# Estágio de execução
FROM openjdk:11-jre-slim

WORKDIR /app

# 4. Copia apenas o JAR construído
COPY --from=builder /app/target/*.jar ./app.jar

# 5. Define o comando de execução
CMD ["java", "-jar", "app.jar"]
