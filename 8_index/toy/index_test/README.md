# 📊 대용량 로그 검색 시스템

## 프로젝트 개요
MySQL B-Tree 인덱스의 성능을 테스트하기 위한 대용량 로그 검색 시스템입니다.  
천만 건 이상의 로그 데이터에서 B-Tree 인덱스의 검색 성능을 실험하고 최적화 방법을 학습합니다.

## 목적
- B-Tree 인덱스 구조와 동작 원리 학습
- 대용량 데이터에서의 인덱스 성능 측정
- 복합 인덱스의 컬럼 순서에 따른 성능 차이 실험
- Spring Data JPA 쿼리 최적화 학습

## 기술 스택
- **Backend**: Spring Boot 3.2
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle
- **Java Version**: 17

### 주요 의존성
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- mysql-connector-java
- lombok
- spring-boot-starter-test

## 프로젝트 구조
```
```

## API 엔드포인트

### 로그 검색 API
- `GET /api/logs` - 전체 로그 조회
- `GET /api/logs/search` - 조건별 검색
- `GET /api/logs/performance` - 인덱스 성능 테스트
- `POST /api/logs/generate` - 테스트 데이터 생성

## 데이터베이스 스키마

### logs 테이블
- **id**: Primary Key (BIGINT, AUTO_INCREMENT)
- **timestamp**: 로그 발생 시간 (DATETIME)
- **level**: 로그 레벨 (VARCHAR)
- **service**: 서비스명 (VARCHAR)
- **user_id**: 사용자 ID (BIGINT)
- **message**: 로그 메시지 (TEXT)
- **response_time**: 응답 시간 (INTEGER)

### 인덱스 구성
- Primary Key: id (클러스터드 인덱스)
- idx_timestamp: timestamp 단일 인덱스
- idx_user_service: (user_id, service) 복합 인덱스
- idx_level_time: (level, timestamp) 복합 인덱스

#### 실험용 인덱스 (컬럼 순서 비교)
- idx_user_time: (user_id, timestamp) 복합 인덱스
- idx_time_user: (timestamp, user_id) 복합 인덱스

## 실험 내용

### 1. 단일 컬럼 인덱스 성능
- timestamp 범위 검색
- user_id 동등 검색

### 2. 복합 인덱스 컬럼 순서
- (user_id, timestamp) vs (timestamp, user_id)
- 선택도(Selectivity)에 따른 성능 차이

### 3. 커버링 인덱스
- 인덱스만으로 쿼리 처리
- JPA 프로젝션 활용

## 성능 테스트
```bash
# JUnit 테스트 실행
./gradlew test

# 특정 테스트만 실행
./gradlew test --tests IndexPerformanceTest
```
## 시스템 요구사항
- Java 17 이상
- MySQL 8.0 이상
- RAM: 최소 8GB (권장 16GB)
- 디스크: 20GB 이상 여유 공간

## 참고사항
- 초기 실행 시 DataInitializer가 1000만 건의 테스트 데이터 생성
- 모든 성능 측정은 JPA 2차 캐시 비활성화 상태에서 수행
  - 이는 순수한 데이터베이스 인덱스 성능만을 측정하기 위함이다.
- Query Plan은 MySQL EXPLAIN을 통해 확인

## License
MIT