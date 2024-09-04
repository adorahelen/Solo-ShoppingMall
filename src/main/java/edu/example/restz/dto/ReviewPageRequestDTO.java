package edu.example.restz.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageRequestDTO {

    @Min(1)
    @Builder.Default
    private int page = 1;

    @Min(5)
    @Max(100)
    @Builder.Default
    private  int size = 5;

    private Long pno;

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page-1, size, sort);
    }

}
