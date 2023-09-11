FROM gradle:jdk17 as BUILDER

RUN apt update && apt install zip

WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Final image
FROM openjdk:21-ea-17-jdk-slim-buster

WORKDIR /app
COPY --from=BUILDER /app/build/libs/application.jar /app/app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]