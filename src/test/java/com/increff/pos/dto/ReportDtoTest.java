package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.InventoryReportData;
import com.increff.pos.model.Data.SalesReportData;
import com.increff.pos.model.Form.*;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    private ReportDto reportDto;
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
    private ReportForm createForm() throws ApiException {
        ZonedDateTime endDate=ZonedDateTime.now().plusDays(1);
        ZonedDateTime startDate=endDate.minusDays(2);
        return createForm(startDate.toString(),endDate.toString(),"nike","shoes");
    }
    private ReportForm createForm(String startDate,String endDate,String brand,String category) throws ApiException {
        ReportForm reportForm=new ReportForm();
        reportForm.setStartDate(startDate);
        reportForm.setEndDate(endDate);
        reportForm.setBrand(brand);
        reportForm.setCategory(category);
        return reportForm;
    }

//    @Test
//    public void testSalesReport() throws Exception {
//        BrandForm brandPojo=createBrand();
//        brandDto.add(brandPojo);
//        productDto.add(createProduct());
//        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());
//        orderDto.add();
//        List<OrderItemForm> list=new ArrayList<>();
//        list.add(createOrder("test1",1,100));
//        orderItemDto.add(list);
//        List<SalesReportData> salesReportData=reportDto.salesReport(createForm());
//
//        assertEquals(1,salesReportData.size());
//        assertEquals("nike",salesReportData.get(0).getBrand());
//        assertEquals("shoes",salesReportData.get(0).getCategory());
//        assertEquals(100.0, salesReportData.get(0).getRevenue(), 0.0);
//    }

    @Test
    public void testInventoryReport() throws Exception {
        BrandForm brandPojo=createBrand();
        brandDto.add(brandPojo);
        productDto.add(createProduct());
        inventoryDto.add(productDto.getByBarcode("test1").getId(),createInventory());

        List<InventoryReportData> list=reportDto.inventoryReport();

        assertEquals(1,list.size());
        assertEquals("nike",list.get(0).getBrand());
        assertEquals("shoes",list.get(0).getCategory());
        assertEquals(100,list.get(0).getQuantity(),0.0);

    }
}
