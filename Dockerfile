# 1. 베이스 이미지
FROM openjdk:21-jdk-slim

# 2. 작업 디렉토리
WORKDIR /app

# 3. Gradle 빌드 후 jar 복사
COPY build/libs/*.jar app.jar

# 4. 앱 실행
ENTRYPOINT ["java","-jar","app.jar"]