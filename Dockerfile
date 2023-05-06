FROM openjdk:17-jdk-alpine
COPY build/libs/micro_api_monolito-1.0.0.jar micro-1.0.0.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","micro-1.0.0.jar"]