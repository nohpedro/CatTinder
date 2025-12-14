# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /workspace
COPY . .

# Fix típico Windows (CRLF) y permisos para el wrapper de Maven
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Construye solo el módulo del config-server (ajusta la ruta del pom si es necesario)
RUN ./mvnw -f config-server/pom.xml -Dmaven.test.skip=true package


# --- runtime ---
FROM eclipse-temurin:17-jre-alpine

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8888 \
    SPRING_APPLICATION_NAME=config-server

# Crear usuario no root
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# Copiamos el JAR construido en la etapa builder
# Ajusta la ruta si tu módulo/jar se llama distinto
COPY --from=builder /workspace/config-server/target/*.jar /app/app.jar

USER app

# Puerto donde expone el Config Server
EXPOSE 8888

# Comando de arranque
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
