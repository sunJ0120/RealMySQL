# 🚀 MySQL 인덱스 성능 최적화 프로젝트

## 📊 프로젝트 개요
10만 건의 로그 데이터에서 MySQL 인덱스의 성능 영향을 정량적으로 분석한 학습 프로젝트입니다.  
인덱스 유무에 따른 쿼리 성능 차이를 실제 측정하여 데이터베이스 최적화의 중요성을 학습했습니다.

## 🎯 핵심 성과

### 최대 83배 성능 개선 달성

| 검색 유형 | Before | After | 개선율 |
|---------|--------|-------|--------|
| **Message 검색** | 167ms | 2ms | **83배** 🥇 |
| **UserId 검색** | 145ms | 3ms | **48배** |
| **Level+Message 복합** | 189ms | 4ms | **47배** |
| **Level+UserId 복합** | 132ms | 5ms | **26배** |
| **Level 검색** | 691ms | 397ms | **1.7배** |

**평균 약 41배의 성능 개선**

## 💡 핵심 인사이트

### 1. LIKE 패턴에 따른 극명한 차이
```sql
-- 인덱스 활용 가능
LIKE '검색어%'  → 14ms  ✅

-- 인덱스 무용지물  
LIKE '%검색어%' → 218ms ❌

→ 15배 성능 차이!
```

### 2. 카디널리티와 인덱스 효과
- **높은 카디널리티** (결과 8개): 48배 개선
- **낮은 카디널리티** (결과 24,975개): 1.7배 개선
- 선택도가 높을수록 인덱스 효과 극대화

### 3. 복합 인덱스 최적화
- 결과가 적은 경우: 단일 인덱스로도 충분 (3~5ms)
- MySQL이 효율적으로 인덱스 선택 및 활용

## 🛠 기술 스택
- **Backend**: Spring Boot 3.x, Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA, Querydsl
- **Build Tool**: Gradle

## 📁 주요 구조
```
├── controller/LogController.java    # 검색 및 성능 테스트 API
├── service/LogService.java          # 성능 측정 로직
├── repository/LogRepository.java    # JPA Repository
├── domain/Logs.java                 # 로그 엔티티
└── dto/PerformanceTestResponse.java # 성능 측정 결과
```

## 🗄️ 데이터베이스

### logs 테이블
```sql
CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    level VARCHAR(10) NOT NULL,
    user_id BIGINT,
    message TEXT
);
```

### 적용한 인덱스
```sql
-- 단일 인덱스
CREATE INDEX idx_logs_level ON logs(level);
CREATE INDEX idx_logs_user_id ON logs(user_id);
CREATE INDEX idx_logs_message ON logs(message(255));

-- 복합 인덱스
CREATE INDEX idx_logs_level_user_id ON logs(level, user_id);
CREATE INDEX idx_logs_level_message ON logs(level, message(255));
```

## 🔬 API 엔드포인트

### 조건별 검색
```http
GET /api/logs/search?level=ERROR&userId=1&message=검색어
```

### 성능 테스트
```http
GET /api/logs/performance
```

**Response:**
```json
{
    "totalRecords": 100000,
    "levelSearchTime": 397,
    "userIdSearchTime": 3,
    "messageSearchTime": 2,
    "messageContainingSearchTime": 218,
    "levelAndUserIdSearchTime": 5,
    "levelAndMessageSearchTime": 4,
    "levelSearchCount": 24975,
    "userIdSearchCount": 8
}
```

## 🧪 실험 방법

### 정확한 성능 측정을 위한 설정
```java
// 1차 캐시 제거
entityManager.clear();

// 측정
long startTime = System.currentTimeMillis();
List<Logs> result = logRepository.findByLevel(Level.ERROR);
long executionTime = System.currentTimeMillis() - startTime;
```

```yaml
# 2차 캐시 비활성화
spring.jpa.properties.hibernate.cache:
  use_second_level_cache: false
  use_query_cache: false
```

### 실행 계획 확인
```sql
EXPLAIN SELECT * FROM logs WHERE level = 'ERROR';
-- type: ALL (Full Scan) → ref (Index 사용) 확인
```

## 📈 학습한 인덱스 설계 원칙

1. **카디널리티가 높은 컬럼** 우선 인덱스 생성
2. **WHERE 절에 자주 사용되는 컬럼** 선택
3. **LIKE 패턴 주의**: 앞에 %가 있으면 인덱스 무용지물
4. **TEXT 타입**: Prefix 인덱스 활용 (예: message(255))
5. **복합 인덱스 순서**: 동등 조건 → 범위 조건

## 🚀 실행 방법

```bash
# 1. 데이터베이스 설정
mysql -u root -p
CREATE DATABASE indextest;

# 2. application.yml 설정
spring.datasource.url=jdbc:mysql://localhost:3306/indextest

# 3. 실행 (10만 건 자동 생성)
./gradlew bootRun

# 4. 성능 테스트
curl http://localhost:8085/api/logs/performance
```

## 💻 시스템 요구사항
- Java 17+
- MySQL 8.0+
- RAM 8GB+

## 📚 참고 자료
- Real MySQL 8.0 - 8장 인덱스
- MySQL 공식 문서 - B-Tree Index

## 📝 License
MIT