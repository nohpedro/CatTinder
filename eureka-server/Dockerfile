# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos los poms primero para cachear dependencias
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml
COPY config-server/pom.xml config-server/pom.xml

# Descarga dependencias
RUN mvn -q -T 1C -e -U dependency:go-offline

# Copiamos el código fuente completo
COPY . .

# Construimos SOLO eureka-server (sin tests)
RUN mvn -q -pl eureka-server -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/eureka-server/target/eureka-server-1.0.0.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/app/app.jar"]