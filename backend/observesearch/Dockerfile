FROM gradle:8.2.1-jdk11 as build
WORKDIR /app

COPY ./backend/observesearch/settings.gradle ./
COPY ./backend/observesearch/build.gradle ./
COPY ./backend/observesearch/src ./src

RUN gradle build -x test --parallel

FROM azul/zulu-openjdk:11
WORKDIR /app

COPY --from=build /app/build/libs/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]