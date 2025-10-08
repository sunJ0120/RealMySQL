package sunj.index_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Logs> findByMessageContaining(String message);

    // 전문 검색
    // mysql 전용 함수라 querydsl로는 구현이 어려워서, 네이티브 쿼리로 작성
    @Query(value = "SELECT * FROM logs " +
            "WHERE MATCH(message) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    List<Logs> searchByFullText(@Param("keyword") String keyword);
}
