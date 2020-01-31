FROM openjdk:8-alpine

RUN mkdir -p /4ncov
WORKDIR /4ncov

COPY target/4ncov-0.0.1-SNAPSHOT.jar /4ncov/

CMD java -jar /4ncov/4ncov-0.0.1-SNAPSHOT.jar
