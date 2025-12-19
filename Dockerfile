# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom and download dependencies first (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build WAR
RUN mvn clean package -DskipTests


# ---------- RUNTIME STAGE ----------
FROM tomcat:11.0-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from build stage
COPY --from=build /app/target/leave.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
