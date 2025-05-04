# Estágio de build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. Cache de dependências (otimização)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2. Copia o código e builda
COPY src ./src
RUN mvn -B package -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.compile.fork=true

# Estágio de execução
FROM eclipse-temurin:17-jre
WORKDIR /app

# 3. Copia apenas o JAR (segurança)
COPY --from=builder /app/target/*.jar app.jar

# 4. Variáveis de ambiente (boas práticas)
ENV BOT_TOKEN=${BOT_TOKEN} \
    JAVA_OPTS="-Xmx256m"

# 5. Entrypoint seguro
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar --token=${BOT_TOKEN}"]
