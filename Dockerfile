# Usa uma imagem base do Java compatível com ARM64
FROM --platform=linux/arm64 eclipse-temurin:21-jdk-jammy

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Cria um usuário não-root para segurança
RUN adduser --system --group appuser && chown -R appuser:appuser /app
USER appuser

# Copia o arquivo .jar para o contêiner
COPY target/rest-api-with-java-0.0.1-SNAPSHOT.jar /app/

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/rest-api-with-java-0.0.1-SNAPSHOT.jar"]