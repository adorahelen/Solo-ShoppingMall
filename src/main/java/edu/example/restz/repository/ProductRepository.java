package edu.example.restz.repository;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.repository.search.ProductSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

//    @EntityGraph(attributePaths = {"images"},
//    type= EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT p FROM  Product p WHERE p.pno = :pno")
//    Optional<Product> getProduct(@Param("pno")Long pno);

    @Query("SELECT p FROM  Product p JOIN FETCH p.images pi WHERE p.pno = :pno")
    Optional<Product> getProduct(@Param("pno")Long pno);

    // tbl_product + tbl_product_image 를 조인하여
    // 지정된 상품번호와 ProductDTO 를 반환하는 getProdictDTO 메서

    @Query("select p from Product  p join fetch p.images pi where p.pno = :pno")
    Optional<ProductDTO> getProductDTO(@Param("pno")Long pno);
}
