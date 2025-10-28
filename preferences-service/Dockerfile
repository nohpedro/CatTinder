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

RUN mvn -q -pl preferences-service -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/preferences-service/target/preferences-service-1.0.0.jar app.jar

# este servicio sí lo vimos en logs corriendo en 8082
EXPOSE 8082

ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cattinder
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

ENTRYPOINT ["java","-jar","/app/app.jar"]
