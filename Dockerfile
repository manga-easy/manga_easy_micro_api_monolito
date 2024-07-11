FROM openjdk:21-ea-17-jdk-slim-buster
COPY build/libs/application.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]