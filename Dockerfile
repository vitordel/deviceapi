# Use a Java 21 base image
FROM eclipse-temurin:21-jdk

# Set work directory
WORKDIR /app

# Copy built jar file
COPY target/deviceapi-*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]