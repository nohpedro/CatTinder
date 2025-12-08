# ===== STAGE 1: build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY users-service/pom.xml users-service/pom.xml
COPY preferences-service/pom.xml preferences-service/pom.xml
COPY intereses-service/pom.xml intereses-service/pom.xml

COPY . .

RUN mvn -q -pl api-gateway -am clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/api-gateway/target/api-gateway-1.0.0.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar","/app/app.jar"]
