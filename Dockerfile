# Estágio de construção
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. Cache otimizado para dependências
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2. Copia o código fonte
COPY src ./src

# 3. Build com configurações robustas
ARG BOT_TOKEN
RUN mvn -B package \
    -DskipTests \
    -Dbot.token=${BOT_TOKEN} \
    -Dmaven.javadoc.skip=true

# Estágio de execução
FROM eclipse-temurin:17-jre
WORKDIR /app

# 4. Copia apenas o artefato final
COPY --from=builder /app/target/FuriaCS-ChatBot-*.jar app.jar

# 5. Configuração segura
ENV BOT_TOKEN=${BOT_TOKEN} \
    JAVA_OPTS="-Xmx256m -XX:+UseContainerSupport"

# 6. Entrypoint otimizado
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar"]
