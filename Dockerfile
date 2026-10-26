# Multi-stage Dockerfile for BAREILLY CITY BANK OF INDIA (Render & Cloud Optimized)

# Stage 1: Build Java 17 WAR Package using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and source files
COPY pom.xml .
COPY src ./src

# Compile and package WAR file
RUN mvn clean package -DskipTests

# Stage 2: Deploy WAR file to Tomcat 9 Application Server
FROM tomcat:9.0-jre17-temurin
WORKDIR /usr/local/tomcat

# Remove default ROOT application
RUN rm -rf webapps/ROOT webapps/ROOT.war

# Copy compiled WAR artifact from builder stage as ROOT.war
COPY --from=builder /app/target/BAREILLYCITYBANKOFINDIA.war webapps/ROOT.war

# Default Environment Variables
ENV PORT=10000
ENV DB_URL=jdbc:mysql://localhost:3306/bareilly_bank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV DB_USERNAME=root
ENV DB_PASSWORD=rootpassword

EXPOSE 10000 8080

# Configure Tomcat port dynamically to match Render's $PORT before launching
CMD sh -c "sed -i 's/port=\"8080\"/port=\"'${PORT:-10000}'\"/g' conf/server.xml && exec catalina.sh run"
