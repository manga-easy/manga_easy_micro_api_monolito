FROM openjdk:21-ea-17-jdk-slim-buster
COPY application.jar application.jar
COPY adc.json src/main/resources/adc.json
ENTRYPOINT ["java","-jar","application.jar"]