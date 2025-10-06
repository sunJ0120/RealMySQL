package sunj.index_test.repository;

import sunj.index_test.domain.Level;
import sunj.index_test.entity.Logs;

import java.time.LocalDateTime;
import java.util.List;

public interface LogQueryRepository {
    List<Logs> searchByConditions(LocalDateTime dateTime, Level level, String message, Long userId);
}
