package edu.example.restz.dto;


import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.entity.Todo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long pno;
    private Long reviewCount;


    @NotEmpty
    private String pname;

    @Min(0)
    private int price;

    private String description;

    @NotEmpty
    private String registerId;

    private List<String> images;


    public ProductDTO(Product product){
        this.pno = product.getPno();
        this.pname = product.getPname();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.registerId = product.getRegisterId();
        this.images = product.getImages()
                .stream()
                .map(ProductImage::getFilename)
                .collect(Collectors.toList());
    }

    public Product toEntity() { // 이거 디비에 넣을떄 엔티티로 바꾸는 거지  반환값을 보아라
        Product product
                = Product.builder()
                .pno(pno)
                .pname(pname)
                .price(price)
                .description(description)
                .registerId(registerId)
                .build();

        if(images != null || !images.isEmpty() ){
            images.forEach(product::addImage);
        }
        return product;
    }
}
