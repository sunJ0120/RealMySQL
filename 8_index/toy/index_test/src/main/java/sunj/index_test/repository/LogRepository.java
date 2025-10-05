package sunj.index_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunj.index_test.entity.Logs;

public interface LogRepository extends JpaRepository<Logs, Long> {
}
