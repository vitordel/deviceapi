# Use a Java 21 base image
FROM eclipse-temurin:21-jdk

# Set work directory
WORKDIR /app

# Copy built jar file
COPY target/deviceapi-*.jar app.jar

# Run the jar file
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

# Run the wait-for-it script before starting your app
ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "db:5432", "--", "java", "-jar", "app.jar"]