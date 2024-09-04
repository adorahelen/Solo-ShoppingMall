package edu.example.restz.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {// 서비스, 컨트롤러(뷰-모델 || RestAPi)

    private Long itemNo;
    private Long pno;

    private String pname;

    private int quantity;
    private int price;

    private String image;
}
