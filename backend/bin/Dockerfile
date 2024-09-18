FROM openjdk:17-oracle

WORKDIR /app

COPY target/housingease-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]