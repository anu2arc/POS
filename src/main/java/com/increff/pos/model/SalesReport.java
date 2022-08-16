package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReport {
    private String brand;
    private String category;
    private Integer quantity;
    private Double revenue;
}
