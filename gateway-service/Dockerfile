# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -f gateway-service/pom.xml -Dmaven.test.skip=true package

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8082 \
    SPRING_APPLICATION_NAME=gateway-service \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8762/eureka/
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=builder /workspace/gateway-service/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 8082
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
