package edu.example.restz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 1, PageRequestDTO 설명
@Schema(description = "오늘의 할 일 목록 조회 시 페이징 정보 지정")
public class PageRequestDTO {

    // 2. 필드 설명
    @Schema(description = "표시할 페이지 번호")
    @Builder.Default
    @Min(1)
    private int page = 1; // page number - first page started 0

    // 3. 필드 설명
    @Schema(description = "한 페이지에 표시할 게시물의 수")
    @Builder.Default
    @Min(10)
    @Max(100)
    private int size = 10; // 1 page Have 10 article

    // 페이지번호, 페이지 게시물 수, 정렬 순서를 pageable 객체로 반환
    public Pageable getPageable(Sort sort) {
        int pageNum = page < 0 ? 1 : page -1;
        int sizeNum = size < 10 ? 10 : size;

        return PageRequest.of(pageNum, sizeNum, sort);

    }

}
