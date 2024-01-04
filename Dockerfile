# Use an official OpenJDK runtime as a parent image
FROM openjdk:18

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
USER root
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

ENV SPRING_DATASOURCE_URL=jdbc:mysql://vm.cloud.cbh.kth.se:2511/userDB
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=12345679
ENV HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect

COPY src ./src

EXPOSE 5001

CMD ["./mvnw", "spring-boot:run"]
