# Build stage (with Maven and JDK 17)
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline -B

COPY src ./src
# Build the application
RUN mvn package -DskipTests

# Runtime stage (with JRE only)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the built JAR from builder stage
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Environment variables
ENV JAVA_OPTS="-Xmx512m -Dfile.encoding=UTF-8"

EXPOSE 8080
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]

# Entry point
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
