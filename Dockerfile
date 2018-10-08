FROM maven:3-jdk-8 AS builder
WORKDIR /usr/share/app
COPY pom.xml ./pom.xml
ADD src ./src
RUN mvn clean install

FROM openjdk:8
ENV JAVA_OPTS=""
WORKDIR /usr/share/app
COPY docker/run.sh ./run.sh
COPY --from=builder /usr/share/app/target/login-ms-*.jar ./app.jar
CMD ["/usr/share/app/run.sh"]