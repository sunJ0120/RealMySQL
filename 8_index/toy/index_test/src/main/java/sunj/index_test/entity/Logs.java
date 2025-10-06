package sunj.index_test.entity;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sunj.index_test.domain.Level;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor //JPA는 기본 생성자 기반
@Getter
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime; //시간 범위 검색 실험

    @Column(nullable = false, length = 10)
    @Enumerated(STRING) //STRING으로 저장하도록 변환
    private Level level; //ERROR, WARN, INFO

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; //실제 데이터 크기 늘리기용 데이터

    @Column(nullable = false)
    private Long userId; //복합 인덱스 실험용
}
