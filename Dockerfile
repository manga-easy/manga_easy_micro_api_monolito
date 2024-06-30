FROM openjdk:17-jdk-alpine
COPY build/libs/application.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]