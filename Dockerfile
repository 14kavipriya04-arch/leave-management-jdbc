FROM tomcat:11.0.15-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/leave.war /usr/local/tomcat/webapps/leave.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
