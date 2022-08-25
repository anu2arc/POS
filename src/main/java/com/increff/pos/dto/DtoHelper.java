package com.increff.pos.dto;

import com.increff.pos.model.Data.*;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.model.Form.ProductForm;
import com.increff.pos.pojo.*;
import org.springframework.stereotype.Repository;

@Repository
public class DtoHelper {
    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }
    public static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }
    public static InventoryData convert(InventoryPojo p) {
        InventoryData d = new InventoryData();
        d.setId(p.getId());
        d.setQuantity(p.getQuantity());
        return d;
    }
    public static InventoryPojo convert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        return p;
    }
    public static OrderData convert(OrderPojo p){
        OrderData d= new OrderData();
        d.setId(p.getId());
        d.setTime(p.getTime());
        return d;
    }
    public static OrderItemData convert(OrderItemPojo f) {
        OrderItemData od=new OrderItemData();
        od.setId(f.getId());
        od.setQuantity(f.getQuantity());
        od.setSellingPrice(f.getSellingPrice());
        od.setProductId(f.getProductId());
        od.setOrderId(f.getOrderId());
        return od;
    }
    public static OrderItemPojo convert(OrderItemForm f, int orderId, Integer productId) {
        OrderItemPojo p=new OrderItemPojo();
        p.setProductId(productId);
        p.setQuantity(f.getQuantity());
        p.setSellingPrice(f.getSellingprice());
        p.setOrderId(orderId);
        return p;
    }
    public static ProductData convert(ProductPojo p) {
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setBarcode(p.getBarcode());
        d.setBrandCategory(p.getBrandCategory());
        d.setMrp(p.getMrp());
        return d;
    }
    public static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        p.setBarcode(f.getBarcode());
        p.setMrp(f.getMrp());
        return p;
    }
}
