FROM openjdk:21-ea-17-jdk-slim-buster
COPY application.jar application.jar
COPY adc.json src/main/resources/adc.json
COPY config src/main/resources/config
COPY profile.pem src/main/resources/profile.pem
ENTRYPOINT ["java","-jar","application.jar"]