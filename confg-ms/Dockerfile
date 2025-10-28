# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
# Cambia "Config-service" si tu módulo tiene otro nombre
RUN ./mvnw -f Config-service/pom.xml -Dmaven.test.skip=true package

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8082 \
    SPRING_APPLICATION_NAME=config-service \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/config-db \
    SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=admin \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8762/eureka/
# Usuario no-root
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
# Cambia "Config-service" si tu módulo tiene otro nombre
COPY --from=builder /workspace/Config-service/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 8082
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
