FROM eclipse-temurin:17-jre

WORKDIR /app
COPY build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.config.location=/application.yml", "-jar", "/app/app.jar"]