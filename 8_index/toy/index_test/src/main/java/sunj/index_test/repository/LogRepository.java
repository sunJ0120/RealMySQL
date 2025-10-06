package sunj.index_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunj.index_test.domain.Level;
import sunj.index_test.entity.Logs;

import java.util.List;

public interface LogRepository extends JpaRepository<Logs, Long>, LogQueryRepository {
    List<Logs> findByLevel(Level level);

    List<Logs> findByUserId(Long userId);

    List<Logs> findByMessage(String message);

    List<Logs> findByLevelAndUserId(Level level, Long userId);

    List<Logs> findByLevelAndMessage(Level level, String message);

    List<Logs> findByMessageStartingWith(String message);

    List<Logs> findByLevelAndMessageStartingWith(Level level, String message);

    List<Logs> findByLevelAndMessageContaining(Level level, String message);
}
