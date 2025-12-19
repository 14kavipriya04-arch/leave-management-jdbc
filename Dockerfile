# Use official Tomcat 11 with Java 17
FROM tomcat:11.0-jdk17

# Remove default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR built by Maven into Tomcat
COPY target/leave.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
