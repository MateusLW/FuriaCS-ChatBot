# Estágio 1: Build (compilação com Maven)
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# 1. Copia apenas o POM primeiro (cache mais eficiente)
WORKDIR /app
COPY pom.xml .
COPY src ./src

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS

ARG BOT_TOKEN
ENV BOT_TOKEN=$BOT_TOKEN

RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests

# --------------------------------------------------------

# Estágio 2: Runtime (imagem final leve)
FROM eclipse-temurin:17-jre-alpine

# 3. Configura ambiente
WORKDIR /app
COPY --from=builder /app/target/*-jar-with-dependencies.jar ./app.jar

# 4. Otimizações para containers
ENV JAVA_OPTS=$JAVA_OPTS

# 5. Comando de execução
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
