package edu.example.restz.repository.search;

import com.querydsl.jpa.JPQLQuery;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.QProduct;
import edu.example.restz.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable){

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, productImage); // 조인

        // where 조건절
        query.where(productImage.ino.eq(0)); // ino가 0인 이미지 


        return null;
    }
}
