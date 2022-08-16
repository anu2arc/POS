package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFrom {
    private String barcode;
    private String name;
    private String brand;
    private String category;
    private Double mrp;
}
