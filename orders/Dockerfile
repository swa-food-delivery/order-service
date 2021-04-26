FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} orders.jar
ENTRYPOINT ["java", "-jar", "/orders.jar"]

LABEL org.label-schema.name="orders-service"
LABEL org.label-schema.description="Docker image for gateway"
LABEL org.label-schema.docker.cmd="docker run --network='host' orders-service"