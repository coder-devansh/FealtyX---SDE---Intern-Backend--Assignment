# Use official OpenJDK image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy and build app
COPY . /app
RUN ./mvnw clean package -DskipTests

# Expose the port (Render uses PORT env)
EXPOSE 8080

# Run the JAR
CMD ["sh", "-c", "java -jar target/student_services-1.0.0.jar"]
