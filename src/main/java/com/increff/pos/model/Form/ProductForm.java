package com.increff.pos.model.Form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private String barcode;
    private String name;
    private String brand;
    private String category;
    private Double mrp;
}
