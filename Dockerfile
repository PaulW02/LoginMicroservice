# Use an official OpenJDK runtime as a parent image
FROM openjdk:18

WORKDIR /app


COPY .mvn/ .mvn
COPY mvnw pom.xml ./
USER root
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-server-service:3306/userDB?autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=12345679
ENV HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]


