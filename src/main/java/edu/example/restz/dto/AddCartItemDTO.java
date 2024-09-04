package edu.example.restz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartItemDTO {

    private String customer;

    private Long pno;

    private int quantity;

}
