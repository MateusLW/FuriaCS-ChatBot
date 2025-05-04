FROM maven:3.8.6-openjdk-17 AS builder

WORKDIR /app

ARG BOT_TOKEN # Recebe via --build-arg
ENV BOT_TOKEN=${BOT_TOKEN} # Transforma em ENV para o Maven

# 1. Copiar apenas o POM primeiro para cache eficiente
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2. Copiar fontes e compilar
COPY src ./src

# 3. Build com configurações robustas
RUN mvn -B \
    -DskipTests \
    -Dmaven.javadoc.skip=true \
    -Dmaven.compile.fork=true \
    clean package

# Estágio de produção
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
