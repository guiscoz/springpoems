FROM openjdk:20-jdk

COPY target/springpoems-0.0.1-SNAPSHOT.jar /app/springpoems.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "springpoems.jar"]
