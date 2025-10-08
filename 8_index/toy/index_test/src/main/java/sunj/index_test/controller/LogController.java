package sunj.index_test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sunj.index_test.domain.FullIndexPerfomanceTestResponse;
import sunj.index_test.domain.Level;
import sunj.index_test.domain.LogResponse;
import sunj.index_test.domain.PerformanceTestResponse;
import sunj.index_test.service.LogService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;
    /**
     * GET /api/logs : 전체 로그 조회
     */
    @GetMapping
    public ResponseEntity<List<LogResponse>> findAll(){
        List<LogResponse> logs = logService.findAll();

        return ResponseEntity.ok(logs);
    }

    /**
     * GET /api/logs/search : 조건별 검색
     *
     * 로그 조회의 핵심으로, 이를 통해 해당 요소를 인덱스로 설정했을때, 성능이 얼마나 빨라지는지를 알 수 있다.
     */
    @GetMapping("/search")
    public ResponseEntity<List<LogResponse>> search(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, //dateTime은 controller에서 변환해야 깔끔하다.
            @RequestParam(required = false) Level level, //ENUM 타입 자동 반환
            @RequestParam(required = false) String message,
            @RequestParam(required = false) Long userId
    ){
        List<LogResponse> logs = logService.searchByConditions(dateTime, level, message, userId);

        return ResponseEntity.ok(logs);
    }

    /**
     * GET /api/logs/performance : 인덱스 성능 테스트
     *
     * 인덱스 성능 테스트를 위한 api
     * 이를 통해 인덱스를 생성했을때, 어떻게 생성했는지에 따른 시간을 측정하기 위함이다.
     */
    @GetMapping("/performance")
    public ResponseEntity<PerformanceTestResponse> performance(){
        PerformanceTestResponse log = logService.performanceTest();

        return ResponseEntity.ok(log);
    }

    /**
     * GET /api/logs/performance/full : full 인덱스 성능 테스트
     *
     * 전문 인덱스 성능 테스트를 위한 api
     * query param으로 키워드를 받아서 성능을 측정한다.
     */
    @GetMapping("/performance/full")
    public ResponseEntity<FullIndexPerfomanceTestResponse> performanceWithFullIndex(
            @RequestParam(required = false) String keyword
    ){
        FullIndexPerfomanceTestResponse log = logService.fullIndexPerfomanceTest(keyword);

        return ResponseEntity.ok(log);
    }
}
