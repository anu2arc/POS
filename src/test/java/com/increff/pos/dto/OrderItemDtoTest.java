package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Data.OrderItemData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.model.Form.ProductForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private OrderDto orderDto;

    private BrandForm createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandForm createBrand(String brand, String category){
        BrandForm brandForm=new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
    private ProductForm createProduct(){
        return createProduct("nike","shoes","airmax",1000.0,"test1");
    }
    private ProductForm createProduct(String brand, String category, String name, double mrp, String barcode){
        ProductForm productForm=new ProductForm();
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);
        productForm.setBarcode(barcode);
        return productForm;
    }
    private InventoryForm createInventory(){
        return createInventory("test1",100);
    }
    private InventoryForm createInventory(String barcode,int quantity){
        InventoryForm inventoryForm=new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }
    private OrderItemForm createOrder(){
        return createOrder("test1",10,100.0);
    }
    private OrderItemForm createOrder(String barcode,int quantity,double sellingPrice){
        OrderItemForm orderItemForm=new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setSellingprice(sellingPrice);
        orderItemForm.setQuantity(quantity);
        return orderItemForm;
    }

    @Test
    public void testGetOrder() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderDto.addOrderItem(orderItemFormList);
        OrderData orderPojo=orderDto.getAll().get(0);
        List<OrderItemData> orderItemDataList=orderItemDto.getOrder(orderPojo.getId());

        assertEquals(1,orderItemDataList.size());

    }

}
