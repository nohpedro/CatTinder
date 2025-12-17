# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# copiar los pom.xml de los módulos
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY matches-service/pom.xml matches-service/pom.xml

# descargar dependencias
RUN mvn -q -T 1C -e -U dependency:go-offline

# copiar el resto del código
COPY . .

# compilar solo el módulo matches-service
RUN mvn -q -pl matches-service -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/matches-service/target/matches-service-1.0.0.jar app.jar

EXPOSE 8084

# apuntar a Eureka dentro de Docker
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka

# apuntar a Config Server
ENV SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888

# apuntar a la base de datos dockerizada
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cattinder
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

ENTRYPOINT ["java","-jar","/app/app.jar"]
