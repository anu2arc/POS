package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.OrderItemData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.model.Form.ProductFrom;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.omg.CORBA.UserException;
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
    private ProductFrom createProduct(){
        return createProduct("nike","shoes","airmax",1000.0,"test1");
    }
    private ProductFrom createProduct(String brand,String category,String name,double mrp,String barcode){
        ProductFrom productForm=new ProductFrom();
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
    public void testAdd() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test2",20,200));

        orderItemDto.add(orderItemForms);
    }

    @Test
    public void testAdd1() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test1",20,200));

        try{
            orderItemDto.add(orderItemForms);
        }
        catch (ApiException exception){
            assertEquals("2: Duplicate Product Present",exception.getMessage().trim());
        }
    }

    @Test
    public void testAdd2() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test3",20,200));

        try{
            orderItemDto.add(orderItemForms);
        }
        catch (ApiException exception){
            assertEquals("Invalid barcode :test3",exception.getMessage().trim());
        }
    }

    @Test
    public void testAdd3() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test1",10000,200));

        try{
            orderItemDto.add(orderItemForms);
        }
        catch (ApiException exception){
            assertEquals("Max Quantity for product test1 is :100",exception.getMessage().trim());
        }
    }

    @Test
    public void testAdd4() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test1",20,20000));

        try{
            orderItemDto.add(orderItemForms);
        }
        catch (ApiException exception){
            assertEquals("Selling price cannot be more than MRP for Product :test1",exception.getMessage().trim());
        }
    }

    @Test
    public void testGetOrder() throws Exception {
        brandDto.add(createBrand());
        ProductFrom product1=createProduct();
        ProductFrom product2=createProduct("nike","shoes","jordan",2000.0,"test2");
        productDto.add(product1);
        productDto.add(product2);
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory());

        List<OrderItemForm> orderItemForms=new ArrayList<>();
        orderItemForms.add(createOrder());
        orderItemForms.add(createOrder("test2",10,200));
        orderItemDto.add(orderItemForms);

        List<OrderItemData> data=orderItemDto.getOrder(orderDto.getAll().get(0).getId());

        assertEquals(orderItemForms.size(),data.size());

        assertEquals(orderItemForms.get(0).getQuantity(),data.get(0).getQuantity());
        assertEquals(orderItemForms.get(1).getQuantity(),data.get(1).getQuantity());
    }
}
