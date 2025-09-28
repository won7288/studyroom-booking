# 싱크라이프 백엔드 신입 개발자 과제 — 스터디룸 예약

### 기술 스택

Language: Java 21

Framework: Spring Boot 3.5

Database: PostgreSQL 18

ORM: Spring Data JPA

Infra: Docker, Docker Compose

Test: Postman, Junit5

---

### 실행 방법

git clone후

IDE 터미널에서 /gradlew build -x test 입력후 jar 파일 생성

프로젝트 파일이 바탕화면에 있다는 가정하에 PowerShell에서  cd "$env:USERPROFILE\Desktop\sync-life-studyroom" 후에 cd sync-life-studyroom 한번 더입력

docker compose up -d 입력으로 도커 컨테이너에 올림

docker compose logs -f app로 로그 확인

---

### API 테스트 방법 포스트맨 기준

인증은 Baerer Token으로 해두고 토큰값은 admin-token, user-token-1, user-token-2 ... 으로 진행


#### 1. 회의실 생성 (ADMIN 전용)

POST http://localhost:8080/rooms

Authorization: Bearer admin-token

Content-Type: application/json

{
  
  "name": "회의실A",
  
  "location": "1층",
  
  "capacity": 5

}


#### 2. 회의실 가용성 조회

GET http://localhost:8080/rooms?date=2025-09-28

Authorization: Bearer admin-token or user-token-1


#### 3. 예약 생성 (USER)

POST http://localhost:8080/reservations

Authorization: Bearer user-token-1

Content-Type: application/json

{
  
  "roomId": 1,
  
  "startAt": "2025-09-28T10:00",
  
  "endAt": "2025-09-28T11:00"

}


#### 4. 예약 취소 (OWNER or ADMIN)

DELETE http://localhost:8080/reservations/1

Authorization: Bearer user-token-1

---

### LLM 사용 모델 GPT5

사용구간

프로젝트 시작전 - 과제 요약과 진행전략 세워줘

DB연결과 Doker 코드 부분 - Postgre 와 dokcer 처음 사용해보는데 사용법좀 알려줘 

JPA, Junit 코드 부분 - 동시성 검사를 하기위해 PostgreSQL: btree_gist 확장 + EXCLUDE 제약 적극 활용을 이용해야하는데 이건 무슨 기술이고 관련 코드 작성해줘

Junit 테스트 부분 - 로컬에서 Postgre로 동시성 검사하려는데 db관련 오류가 나는데 해결법

ADR - 중복 예약 방지 전략으로는 어떤게 있고 장단점이 뭐야?

---

### 회고
짧은 시간이었지만 그사이에 백엔드 개발자라면 어떤식으로 사고해야하는가를 배운것 같아 재밌었습니다.

포스트맨이나 H2를 이용해 동시성 검사할수 있으나 포스트맨은 완벽한 동시성 검사가 안되고 H2는 기본적인 단순테스트만 되기에

Junit을 이용해 테스트를 해보려 했지만 db관련 오류가 계속 발생해 해결하지 못해 아쉽습니다.😢

짧은시간 내에 junit을 이용한 테스트 코드 그리고 도커 관련해서 완벽하게 이해는 못했지만 흐름정도는 얼추 이해하게 되어 좋았습니다!
