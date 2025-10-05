package sunj.index_test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sunj.index_test.entity.Logs;

import java.time.LocalDateTime;

/**
 * 일반 log 응답을 위한 response DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogResponse {
    private Long id;
    private LocalDateTime dateTime;
    private Level level;
    private Long userId;
    private String message;

    // Entity > DTO 변환 메서드 정의
    // Entity를 parameter로 받아서 변환한다.
    public static LogResponse from(Logs logs){
        return LogResponse.builder()
                .id(logs.getId())
                .dateTime(logs.getDateTime())
                .level(logs.getLevel())
                .userId(logs.getUserId())
                .message(logs.getMessage())
                .build();
    }
}
