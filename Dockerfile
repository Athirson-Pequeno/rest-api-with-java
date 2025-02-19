# Usa uma imagem base do Java
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo .jar para o contêiner
COPY target/rest-api-with-java-0.0.1-SNAPSHOT.jar /app/rest-api-with-java-0.0.1-SNAPSHOT.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/rest-api-with-java-0.0.1-SNAPSHOT.jar"]
