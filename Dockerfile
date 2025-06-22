FROM ibm-semeru-runtimes:open-21-jdk

# 
RUN mkdir /opt/app
RUN mkdir /opt/config

COPY build/libs/*.jar /opt/app/app.jar

ENV APP_HOME=/opt/app

# Expose the port the app runs on
EXPOSE 8080
# Set the working directory to /opt/app
WORKDIR /opt/app


ENTRYPOINT ["java", "-jar", "/opt/app/app.jar", "--spring.config.location=/opt/config/application.yml"]

