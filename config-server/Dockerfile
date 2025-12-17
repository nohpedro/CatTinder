# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos los POMs primero para cachear dependencias
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY matches-service/pom.xml matches-service/pom.xml

# Descarga dependencias
RUN mvn -q -T 1C -e -U dependency:go-offline

# Copiamos el código fuente completo
COPY . .

# Construimos SOLO config-server (sin tests)
RUN mvn -q -pl config-server -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/config-server/target/config-server-1.0.0.jar app.jar

# Puerto del Config Server
EXPOSE 8888

# Registro en Eureka dentro de Docker
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka

# Configuración del repositorio Git
ENV SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/BLVESMxN/cattinder-config-repo.git
ENV SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULT_LABEL=main

# Clave de encriptación
ENV ENCRYPT_KEY=MySecretKey12345

ENTRYPOINT ["java","-jar","/app/app.jar"]
