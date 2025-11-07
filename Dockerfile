# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Hacer mvnw ejecutable
RUN chmod +x mvnw

# Descargar dependencias (layer caching)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR desde builder
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    VAULT_URI=http://vault:8200 \
    VAULT_TOKEN=dev-root-token-id \
    SPRING_PROFILES_ACTIVE=docker

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]