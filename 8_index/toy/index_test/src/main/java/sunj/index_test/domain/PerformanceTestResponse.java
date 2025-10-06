package sunj.index_test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * index test를 위한 응답을 담은 간단한 dto
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceTestResponse {
    // 전체 로그 개수
    private Long totalRecords;

    // 단일 컬럼 인덱스 테스트
    private Long levelSearchTime;
    private Long userIdSearchTime;
    private Long messageSearchTime;
    // 추가) containing에서 인덱스 실험
    private Long messageContainingSearchTime;

    // 복합 인덱스 테스트
    private Long levelAndUserIdSearchTime;
    private Long levelAndMessageSearchTime;

    // 조회된 결과 개수 (옵션)
    private Integer levelSearchCount;
    private Integer userIdSearchCount;
    private Integer levelAndUserIdSearchCount;

    // 인덱스 존재 여부 (나중에 비교용)
    private String testDescription; // "인덱스 전" or "인덱스 후"
}
