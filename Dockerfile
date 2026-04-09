# Usa una imagen ligera de Jav25
FROM eclipse-temurin:21-jdk-jammy

# Directorio de trabajo
WORKDIR /app

# Copia el jar
COPY target/toka-arena-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar app
ENTRYPOINT ["java","-jar","/app/app.jar"]
