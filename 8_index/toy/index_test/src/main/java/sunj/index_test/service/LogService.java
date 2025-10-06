package sunj.index_test.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sunj.index_test.domain.Level;
import sunj.index_test.domain.LogResponse;
import sunj.index_test.domain.PerformanceTestResponse;
import sunj.index_test.entity.Logs;
import sunj.index_test.repository.LogRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final LogRepository logRepository;
    private final EntityManager entityManager;  // LogService - 직접 사용 용 (캐시 클리어, 네이티브 쿼리 등)

    /**
     * 전체 로그를 조회하기 위한 method
     * @return
     */
    public List<LogResponse> findAll(){
        List<Logs> logEntityList = logRepository.findAll();
        List<LogResponse> logResponseList = getLogResponses(logEntityList);

        return logResponseList;
    }

    /**
     * 조건별로 로그를 조회하기 위한 method
     */
    public List<LogResponse> searchByConditions(
            LocalDateTime dateTime,
            Level level,
            String message,
            Long userId){
        // 파라미터들이 모두 선택 쿼리이기 때문에, 동적 쿼리가 필요.

        List<Logs> logEntityList = logRepository.searchByConditions(dateTime, level, message, userId);
        List<LogResponse> logResponseList = getLogResponses(logEntityList);

        return logResponseList;
    }

    /**
     * 인덱스의 정확한 성능을 검수하기 위한 method
     */
    public PerformanceTestResponse performanceTest(){
        long startTime; //성능 측정을 위한 시작 시간 명시

        // 1. level 검색
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> errorLogs = logRepository.findByLevel(Level.ERROR);
        long levelSearchTime = System.currentTimeMillis() - startTime;

        // 2. userId 검색
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> userLogs = logRepository.findByUserId(1L);
        long userIdSearchTime = System.currentTimeMillis() - startTime;

        // 3. message 검색
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> messageLogs = logRepository.findByMessageStartingWith("가나디"); // LIKE '가나디%' 체크를 위해 StartingWith으로 한다.
        long messageSearchTime = System.currentTimeMillis() - startTime;

        // 3-2. message 검색 2
        // 이번에는, 인덱스가 통하지 않는 contain으로 인덱스의 효과를 측정해보자.
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> messageContainingLogs = logRepository.findByMessageContaining("가나디"); // LIKE '%가나디%' 체크를 위해 Containing으로 한다.
        long messageContainingSearchTime = System.currentTimeMillis() - startTime;

        // 복합 검색 (level + userId)
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> levelAndUserIdLogs = logRepository.findByLevelAndUserId(Level.ERROR, 1L);
        long levelAndUserIdSearchTime = System.currentTimeMillis() - startTime;

        // 복합 검색 (level + message)
        entityManager.clear(); // 각 테스트마다 독립적 측정을 위해 clear 해줘야 한다.
        startTime = System.currentTimeMillis();
        List<Logs> levelAndMessageLogs = logRepository.findByLevelAndMessageStartingWith(Level.ERROR, "가나디");
        long levelAndMessageSearchTime = System.currentTimeMillis() - startTime;

        return PerformanceTestResponse.builder()
                .totalRecords(logRepository.count())
                .levelSearchTime(levelSearchTime)
                .userIdSearchTime(userIdSearchTime)
                .messageSearchTime(messageSearchTime)
                .messageContainingSearchTime(messageContainingSearchTime) //인덱스가 안먹히는지 봐야 한다.
                .levelAndUserIdSearchTime(levelAndUserIdSearchTime)
                .levelAndMessageSearchTime(levelAndMessageSearchTime)
                .levelSearchCount(errorLogs.size())
                .userIdSearchCount(userLogs.size())
                .levelAndUserIdSearchCount(levelAndUserIdLogs.size())
                .testDescription("현재 인덱스 상태")
                .build();
    }

    // entity to dto를 위한 method
    private static List<LogResponse> getLogResponses(List<Logs> logEntityList) {
        List<LogResponse> logResponseList = logEntityList.stream()
                .map(logs -> LogResponse.from(logs))
                .collect(Collectors.toList());
        return logResponseList;
    }
}
