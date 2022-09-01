package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.*;
import com.increff.pos.service.ApiException;
import org.apache.xpath.operations.Or;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDto brandDto;

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
    public void testAdd(){
        orderDto.add();
    }

    @Test
    public void testGetAll(){
        orderDto.add();
        orderDto.add();
        List<OrderData> list=orderDto.getAll();
        assertEquals(2,list.size());
    }

    @Test
    public void testGetById() throws ApiException {
        OrderData data=orderDto.add();
        OrderData orderData=orderDto.getById(data.getId());
        assertEquals(data.getId(),orderData.getId());
        assertEquals(data.getTime(),orderData.getTime());
    }

    @Test
    public void testAddOrderItem() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        productDto.add(createProduct("nike","shoes","alpha",1000.0,"test2"));
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory("test2",200));
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderItemFormList.add(createOrder("test2",20,200.0));
        orderDto.addOrderItem(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testAddOrderItemWithDuplicateOrder() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        productDto.add(createProduct("nike","shoes","alpha",1000.0,"test2"));
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory("test1",200));
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderItemFormList.add(createOrder("test2",20,200.0));
        orderDto.addOrderItem(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testAddOrderItemWithWrongBarcode() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory("test2",200));
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderItemFormList.add(createOrder("test3",20,200.0));
        orderDto.addOrderItem(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testAddOrderItemQuantityCheck() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory("test2",200));
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderItemFormList.add(createOrder("test2",400,200.0));
        orderDto.addOrderItem(orderItemFormList);
    }

    @Test(expected = ApiException.class)
    public void testAddOrderItemMrpCheck() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
        inventoryDto.add(productDto.getByBarcode("test2").getId(),createInventory("test2",200));
        List<OrderItemForm> orderItemFormList=new ArrayList<>();
        orderItemFormList.add(createOrder());
        orderItemFormList.add(createOrder("test2",40,200000.0));
        orderDto.addOrderItem(orderItemFormList);
    }

    @Test
    public void testGetByRange() throws ApiException {
        List<OrderData> orderDataList=new ArrayList<>();
        orderDataList.add(orderDto.add());
        orderDataList.add(orderDto.add());

        OrderForm orderForm=new OrderForm();
        orderForm.setStartDate(orderDataList.get(0).getTime().toString());
        orderForm.setEndDate(orderDataList.get(1).getTime().toString());
        System.out.println(orderForm.getStartDate());
        System.out.println(orderForm.getEndDate());
        List<OrderData> data=orderDto.getByRange(orderForm);
//        List<OrderData> data=orderDto.getAll();

        assertEquals(data.size(),orderDataList.size());

        assertEquals(data.get(0).getTime(),orderDataList.get(0).getTime());
        assertEquals(data.get(0).getId(),orderDataList.get(0).getId());

        assertEquals(data.get(1).getTime(),orderDataList.get(1).getTime());
        assertEquals(data.get(1).getId(),orderDataList.get(1).getId());

    }
}
