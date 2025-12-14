# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -f authentication-service/pom.xml -Dmaven.test.skip=true package

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=5000 \
    SPRING_APPLICATION_NAME=authentication-service \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/review-db \
    SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=admin \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8762/eureka/
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=builder /workspace/authentication-service/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 5000
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]