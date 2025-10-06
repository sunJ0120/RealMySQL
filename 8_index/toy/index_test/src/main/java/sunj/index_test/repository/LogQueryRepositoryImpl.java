package sunj.index_test.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sunj.index_test.domain.Level;
import sunj.index_test.entity.Logs;
import java.time.LocalDateTime;
import java.util.List;

import static sunj.index_test.entity.QLogs.*;

@Repository
@RequiredArgsConstructor
public class LogQueryRepositoryImpl implements LogQueryRepository{
    private final JPAQueryFactory query;

    @Override
    public List<Logs> searchByConditions(LocalDateTime dateTime, Level level, String message, Long userId) {
        return query.selectFrom(logs)
                .where(
                        dateTimeEq(dateTime),
                        levelEq(level),
                        messageEq(message),
                        userIdEq(userId)
                ).fetch();
    }

    //동적 쿼리 작성을 위한 BooleanExpression method들

    /*
    날짜의 경우, 일까지를 범위로 비교하도록 한다.
     */
    public BooleanExpression dateTimeEq(LocalDateTime dateTime){
        // null 처리
        if(dateTime == null){
            return null;
        }

        LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = dateTime.toLocalDate().atStartOfDay().plusDays(1);

        return logs.dateTime.goe(startOfDay)  // greater or equal (>=)
                .and(logs.dateTime.lt(endOfDay));  // less than (<)
    }

    public BooleanExpression levelEq(Level level){
        return level != null ? logs.level.eq(level) : null;
    }

    /*
    메세지의 경우, 긴 타입 이므로 contains으로 비교하게끔 한다.
     */
    public BooleanExpression messageEq(String message){
        return message != null ? logs.message.contains(message) : null;
    }

    public BooleanExpression userIdEq(Long userId){
        return userId != null ? logs.userId.eq(userId) : null;
    }
}
