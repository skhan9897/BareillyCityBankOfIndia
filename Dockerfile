# Multi-stage Dockerfile for BAREILLY CITY BANK OF INDIA

# Stage 1: Build Java 17 WAR Package using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Compile and package WAR file
RUN mvn clean package -DskipTests

# Stage 2: Deploy WAR file to Tomcat 9 Application Server
FROM tomcat:9.0.86-jdk17-temurin-alpine
WORKDIR /usr/local/tomcat

# Remove default ROOT application
RUN rm -rf webapps/ROOT webapps/ROOT.war

# Copy compiled WAR artifact from builder stage as ROOT.war
COPY --from=builder /app/target/BAREILLYCITYBANKOFINDIA.war webapps/ROOT.war

# Set default Environment Variables
ENV PORT=8080
ENV DB_URL=jdbc:mysql://mysql:3306/bareilly_bank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV DB_USERNAME=root
ENV DB_PASSWORD=rootpassword

EXPOSE 8080

# Run Tomcat Server
CMD ["catalina.sh", "run"]
