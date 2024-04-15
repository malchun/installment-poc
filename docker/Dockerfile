FROM openjdk:22-ea-21-slim

EXPOSE 3031

ARG JAR_FILE=target/installment-processor-1.0-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]