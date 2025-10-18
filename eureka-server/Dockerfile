# ============ Fase de construcción ============
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /extracted

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Extrae capas con layertools
RUN java -Djarmode=layertools -jar app.jar extract

# ============ Fase final (runtime) ============
FROM eclipse-temurin:21-jre-alpine
WORKDIR /application

# Usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar capas extraídas desde la fase builder
COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./

EXPOSE 8761

ENTRYPOINT ["java", "-cp", "/application", "org.springframework.boot.loader.launch.JarLauncher"]
