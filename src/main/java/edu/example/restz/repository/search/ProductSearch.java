package edu.example.restz.repository.search;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);
    Page<ProductDTO> listWithAllImages(Pageable pageable);
    Page<ProductDTO> listWithAllImagesFetch(Pageable pageable);
    Page<ProductListDTO> listWithReviewCount(Pageable pageable);
}
