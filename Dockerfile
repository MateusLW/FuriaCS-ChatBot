# Estágio 1: Build (compilação com Maven)
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# 1. Copia apenas o POM primeiro (cache mais eficiente)
WORKDIR /app

ARG BOT_TOKEN
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copia o código-fonte e compila
COPY src ./src
RUN mvn package -DskipTests

# --------------------------------------------------------

# Estágio 2: Runtime (imagem final leve)
FROM eclipse-temurin:17-jre-alpine

# 3. Configura ambiente
WORKDIR /app
COPY --from=builder /app/target/*-jar-with-dependencies.jar ./app.jar

# 4. Otimizações para containers
ENV BOT_TOKEN=${BOT_TOKEN}
ENV JAVA_OPTS="-Xmx512m -Dfile.encoding=UTF-8"

# 5. Comando de execução
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
