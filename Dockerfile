FROM openjdk:14-jdk-alpine
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} musicshopbackend.jar
ENTRYPOINT ["java","-jar","musicshopbackend.jar"]