# 1. 가벼운 JRE 이미지를 기반으로 사용 (Eclipse Temurin 21 JRE)
FROM eclipse-temurin:17-jre

# 2. JAR 파일 복사 (로컬에서 빌드한 JAR을 Docker 이미지로 추가)
COPY build/libs/*.jar app.jar

# 3. 실행 명령 (JAR 실행)
ENTRYPOINT ["java", "-Duser.tsimezone=Asia/Seoul", "-jar", "app.jar"]