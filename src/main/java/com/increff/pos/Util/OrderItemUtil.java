package com.increff.pos.Util;

import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
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
        ProductPojo pp=new ProductPojo();
        try{
            pp=productService.getByBarcode(orderItem.getBarcode());
        }
        catch (ApiException apiException){
            throw new ApiException("Invalid barcode :"+orderItem.getBarcode());
        }
        InventoryPojo ip=inventoryService.get(pp.getId());
        if(ip.getQuantity()< orderItem.getQuantity())
            throw new ApiException("Max Quantity for product "+orderItem.getBarcode()+" is :"+ip.getQuantity());
        if(orderItem.getSellingprice()> pp.getMrp())
            throw new ApiException("Selling price cannot be more than MRP for Product :"+orderItem.getBarcode());
    }
}
