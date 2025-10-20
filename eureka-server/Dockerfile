# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
# Fix típico Windows (CRLF) y permisos
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
# ⬇️ Construye por ruta (sin reactor)
RUN ./mvnw -f eureka-server/pom.xml -Dmaven.test.skip=true package

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8762 \
    SPRING_APPLICATION_NAME=eureka \
    EUREKA_CLIENT_REGISTER_WITH_EUREKA=false \
    EUREKA_CLIENT_FETCH_REGISTRY=false
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=builder /workspace/eureka-server/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 8762
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
