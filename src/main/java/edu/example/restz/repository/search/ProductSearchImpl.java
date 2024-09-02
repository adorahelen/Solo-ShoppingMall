package edu.example.restz.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.QProduct;
import edu.example.restz.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable){

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product).leftJoin(product.images, productImage)
                .where(productImage.ino.eq(0)); // ino가 0인 이미지

        JPQLQuery<ProductListDTO> dtoQuery
        = query.select(Projections.bean(
                ProductListDTO.class,
                product.pno, product.pname, product.price, product.registerId,
                productImage.filename.as("pimage")));

        getQuerydsl().applyPagination(pageable, dtoQuery); // paging *
        List<ProductListDTO> productList = dtoQuery.fetch(); // run query *
        long count = dtoQuery.fetchCount(); // search Record Count


        return new PageImpl<>(productList, pageable, count);
    }
}
