# Estágio de construção
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Etapa 1: Configuração inicial
COPY pom.xml .
RUN mvn -B dependency:resolve

# Etapa 2: Download completo das dependências
RUN mvn -B dependency:go-offline \
    -Dmaven.wagon.http.retryHandler.count=3 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=25

# Etapa 3: Build do projeto
COPY src ./src
ARG BOT_TOKEN
RUN mvn -B package \
    -DskipTests \
    -Dbot.token=${BOT_TOKEN}

FROM eclipse-temurin:17-jre
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]

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
