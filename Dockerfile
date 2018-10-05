FROM maven:3-jdk-8 AS builder
WORKDIR /usr/share/app
COPY pom.xml ./pom.xml
ADD src ./src
RUN mvn clean install

FROM openjdk:8
ENV JAVA_OPTS=""
WORKDIR /usr/share/app
COPY --from=builder /usr/share/app/target/login-ms-*.jar ./app.jar
CMD ["/bin/bash", "-c", "java $JAVA_OPTS -jar app.jar"]