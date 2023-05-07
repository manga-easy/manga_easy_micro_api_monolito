FROM openjdk:21-ea-17-jdk-slim-buster
COPY build/libs/micro_api_monolito-1.0.0.jar micro-1.0.0.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","micro-1.0.0.jar"]