FROM openjdk:8-alpine

ENV ACTIVE_PROFILE ACTIVE_PROFILE

RUN mkdir -p /4ncov
WORKDIR /4ncov

COPY target/4ncov-0.0.1-SNAPSHOT.jar /4ncov/

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} /4ncov/4ncov-0.0.1-SNAPSHOT.jar
