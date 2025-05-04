# Estágio de construção com Maven
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app

# Copiar apenas os arquivos necessários primeiro (para melhor cache)
COPY pom.xml .
COPY src ./src

# Baixar dependências e construir o projeto
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# Estágio de execução final
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar o JAR construído
COPY --from=builder /app/target/furia-chatbot-cs-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Variáveis de ambiente (serão configuradas no docker run)
ENV BOT_TOKEN="7671407981:AAHtT4GgFRKlrsjXGrUY_3CkFt9HxTM_zuc"
ENV BOT_USERNAME="FuriaCSBot"

# Usuário não-root para segurança
RUN groupadd -r javagroup && \
    useradd -r -g javagroup javauser && \
    chown -R javauser:javagroup /app

USER javauser

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
