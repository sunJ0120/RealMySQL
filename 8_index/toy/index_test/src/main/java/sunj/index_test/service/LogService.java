package sunj.index_test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sunj.index_test.domain.Level;
import sunj.index_test.domain.LogResponse;
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

    // entity to dto를 위한 method
    private static List<LogResponse> getLogResponses(List<Logs> logEntityList) {
        List<LogResponse> logResponseList = logEntityList.stream()
                .map(logs -> LogResponse.from(logs))
                .collect(Collectors.toList());
        return logResponseList;
    }
}
