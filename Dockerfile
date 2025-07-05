# Use official OpenJDK image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy everything
COPY . /app

# ✅ Give execute permission to mvnw
RUN chmod +x ./mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose port (Render will set PORT env)
EXPOSE 8080

# Start the app
CMD ["sh", "-c", "java -jar target/student_services-1.0.0.jar"]
