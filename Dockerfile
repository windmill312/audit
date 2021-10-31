FROM openjdk:11
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} audit.jar
ENTRYPOINT ["java", "-jar", "audit.jar"]