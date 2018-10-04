FROM openjdk:8
ENV JAVA_OPTS=""
WORKDIR /usr/share/app
COPY target/login-ms-*.jar ./app.jar
CMD ["/bin/bash", "-c", "java $JAVA_OPTS -jar app.jar"]