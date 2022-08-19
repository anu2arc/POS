package com.increff.pos.Util;

import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemUtil {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    public void validate(OrderItemForm orderItem) throws ApiException {
        if(orderItem.getBarcode().isEmpty())
            throw new ApiException("Barcode cannot be empty");

    }
}
