FROM gradle:jdk17 as BUILDER

RUN apt update && apt install zip

WORKDIR /app
COPY . .
RUN gradle wrapper
RUN ./gradlew build -x test

RUN zip -d /app/build/libs/build.jar 'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*.DSA'

# Final image
FROM openjdk:21-ea-17-jdk-slim-buster

WORKDIR /app
COPY --from=BUILDER /app/build/libs/build.jar /app/app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]