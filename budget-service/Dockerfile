FROM eclipse-temurin:17-jre-alpine
RUN apk update
RUN apk upgrade
WORKDIR /app
COPY target/budget-service-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
