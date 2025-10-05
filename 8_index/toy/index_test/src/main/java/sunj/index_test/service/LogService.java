package sunj.index_test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sunj.index_test.domain.LogResponse;
import sunj.index_test.entity.Logs;
import sunj.index_test.repository.LogRepository;

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

        List<LogResponse> logResponseList = logEntityList.stream()
                .map(logs -> LogResponse.from(logs))
                .collect(Collectors.toList());

        return logResponseList;
    }
}
