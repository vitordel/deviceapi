FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copie o código-fonte ao invés do JAR
COPY . .

# Adicione o wait-for-it
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

# Comando padrão - espera o banco e inicia o app (usando Maven)
ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "db:5432", "--", "sh", "-c", "./mvnw spring-boot:run"]