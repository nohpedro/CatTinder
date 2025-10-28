# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml

RUN mvn -q -T 1C -e -U dependency:go-offline

COPY . .

RUN mvn -q -pl users-service -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/users-service/target/users-service-1.0.0.jar app.jar

# asumo que este servicio corre en 8081 (si corre en otro puerto en application.properties, cámbialo)
EXPOSE 8081

# apuntar a Eureka dentro de Docker
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka

# apuntar a la base de datos dockerizada
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cattinder
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

ENTRYPOINT ["java","-jar","/app/app.jar"]
