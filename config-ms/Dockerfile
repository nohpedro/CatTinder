# --- build ---
# Etapa 1: Construir la aplicación con Maven
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY . .

# Corrige fin de línea y permisos del wrapper de Mav
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Compila SOLO el módulo config-ms, saltando los tests
RUN ./mvnw -f config-ms/pom.xml -Dmaven.test.skip=true clean package

# --- runtime ---
# Etapa 2: Crear la imagen final de ejecución
FROM eclipse-temurin:17-jre-alpine

# Variables de entorno para el microservicio
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=18083 \
    SPRING_APPLICATION_NAME=config-ms \
    SPRING_PROFILES_ACTIVE=prod-docker \
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:18762/eureka/

# Creamos un usuario 'app' para no correr como root (buena práctica de seguridad)
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

# Copia el JAR compilado desde la etapa 'builder'
COPY --from=builder /workspace/config-ms/target/*-SNAPSHOT.jar /app/app.jar

# Cambiamos al usuario 'app'
USER app

# Exponemos el puerto que definimos en las variables de entorno
EXPOSE 18083

#
# --- CORRECCIÓN ---
#
# Aquí forzamos a Spring a *ignorar* la configuración de la base de datos.
# Esto evita el error de contraseña y permite que la aplicación arranque
# para que puedas ver Swagger.
#
# El argumento --spring.autoconfigure.exclude anula cualquier variable de entorno
# (como SPRING_DATASOURCE_PASSWORD) que venga de Docker Compose.
#
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar --spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"]