# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY config-server/pom.xml config-server/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml
COPY intereses-service/pom.xml intereses-service/pom.xml

COPY . .

# Construir el JAR ejecutable del Config Server
# Primero compilamos e instalamos las dependencias del proyecto padre
RUN mvn -pl config-server -am clean install -DskipTests

# Verificar qué JARs se generaron
RUN echo "=== JARs en target ===" && \
    ls -la /app/config-server/target/*.jar 2>/dev/null || echo "No se encontraron JARs" && \
    echo "=== Verificando manifest del JAR principal ===" && \
    cd /app/config-server/target && \
    if [ -f config-server-1.0.0.jar ]; then \
        echo "JAR encontrado, tamaño: $(du -h config-server-1.0.0.jar | cut -f1)" && \
        jar xf config-server-1.0.0.jar META-INF/MANIFEST.MF 2>&1 && \
        if [ -f META-INF/MANIFEST.MF ]; then \
            echo "=== Contenido completo del manifest ===" && \
            cat META-INF/MANIFEST.MF && \
            echo "=== Buscando Main-Class ===" && \
            if grep -i "main-class" META-INF/MANIFEST.MF; then \
                echo "✓ Main-Class encontrado en manifest"; \
            else \
                echo "✗ ERROR: Main-Class NO encontrado en manifest"; \
                echo "El JAR no es ejecutable. Verificando si hay un JAR .original..."; \
                ls -la *.jar 2>/dev/null; \
            fi; \
        else \
            echo "ERROR: No se pudo extraer el manifest del JAR"; \
        fi && \
        rm -rf META-INF; \
    else \
        echo "ERROR: El JAR config-server-1.0.0.jar no se generó"; \
        echo "Archivos en target:"; \
        ls -la /app/config-server/target/ 2>/dev/null || echo "Directorio target no existe"; \
    fi

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR ejecutable generado por spring-boot-maven-plugin
# El plugin 'repackage' reemplaza el JAR original con el JAR ejecutable (fat JAR)
# Este JAR contiene todas las dependencias y el MANIFEST.MF con Main-Class
COPY --from=build /app/config-server/target/config-server-1.0.0.jar app.jar

EXPOSE 8888

ENV CONFIG_REPO_PATH=/config-repo
ENV CONFIG_GIT_URI=file:///config-repo
ENV CONFIG_GIT_BRANCH=main
ENV CONFIG_SERVER_USER=configuser
ENV CONFIG_SERVER_PASSWORD=configpass

ENTRYPOINT ["java","-jar","/app/app.jar"]



