package edu.example.restz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyDeleteCartItemDTO {
    private Long itemNo;
    private int quantity;

}
