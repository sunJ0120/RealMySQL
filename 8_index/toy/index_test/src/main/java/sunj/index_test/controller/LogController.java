package sunj.index_test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sunj.index_test.domain.LogResponse;
import sunj.index_test.service.LogService;

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
     */

    /**
     * GET /api/logs/performance : 인덱스 성능 테스트
     */
}
