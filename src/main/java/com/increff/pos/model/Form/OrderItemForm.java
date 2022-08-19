package com.increff.pos.model.Form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private String barcode;
    private Integer quantity;
    private Double sellingprice;
}
