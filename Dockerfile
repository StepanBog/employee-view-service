FROM maven:3.8.1-jdk-11 AS builder
ADD ./ /app

WORKDIR /app

RUN mvn clean package -s settings.xml -Dfile.encoding=UTF-8 -DskipTests=true 

FROM openjdk:11

ENV spring.application.name="odp-admin"
ENV grpc.server.port="9111"

COPY --from=builder /app/target/odp-admin-1.0.0-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
