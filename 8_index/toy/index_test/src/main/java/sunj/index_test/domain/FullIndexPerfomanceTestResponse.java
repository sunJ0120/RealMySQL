package sunj.index_test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * full index test를 위한 응답을 담은 간단한 dto
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullIndexPerfomanceTestResponse {
    // 검색 소요 시간
    private Long likeSearchTime;           // LIKE '%keyword%' 검색 시간
    private Long fullTextSearchTime;       // MATCH AGAINST 검색 시간

    // 검색 결과
    private Integer likeResultCount;       // LIKE 검색 결과 개수
    private Integer fullTextResultCount;   // 전문 검색 결과 개수

    // 성능 비교
    private Double performanceImprovement; // 성능 향상 비율 (LIKE 시간 / FULL TEXT 시간)

    // 테스트 정보
    private String testDescription;        // "인덱스 전" or "인덱스 후"
    private String keyword;                // 검색어
}
