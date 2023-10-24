FROM gradle:8.2.1-jdk11 as build
WORKDIR /app

COPY ./backend/example/settings.gradle ./
COPY ./backend/example/build.gradle ./
COPY ./backend/example/src ./src

RUN gradle clean build -x test --parallel

FROM azul/zulu-openjdk:11
WORKDIR /app

COPY --from=build /app/build/libs/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]