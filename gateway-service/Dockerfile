# --- build ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -f config-server/pom.xml -Dmaven.test.skip=true package

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8888 \
    SPRING_APPLICATION_NAME=config-server
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=builder /workspace/config-server/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 8888
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
