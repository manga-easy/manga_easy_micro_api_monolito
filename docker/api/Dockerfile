FROM openjdk:21-ea-17-jdk-slim-buster
COPY micro_api_monolito-1.1.0.jar micro-1.1.0.jar
COPY adc.json src/main/resources/adc.json
ENTRYPOINT ["java","-jar","micro-1.1.0.jar"]