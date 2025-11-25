# Imagen ligera con Java 17
FROM eclipse-temurin:17-jre-alpine

# Argumento con la ruta del jar generado por Maven
ARG JAR_FILE=target/*.jar

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar del build local al contenedor
COPY ${JAR_FILE} app.jar

# Puerto donde expone el Config Server
EXPOSE 8888

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
