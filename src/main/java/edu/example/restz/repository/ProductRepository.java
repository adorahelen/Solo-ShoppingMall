package edu.example.restz.repository;

import edu.example.restz.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    @EntityGraph(attributePaths = {"images"},
//    type= EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT p FROM  Product p WHERE p.pno = :pno")
//    Optional<Product> getProduct(@Param("pno")Long pno);

    @Query("SELECT p FROM  Product p JOIN FETCH p.images pi WHERE p.pno = :pno")
    Optional<Product> getProduct(@Param("pno")Long pno);
}
