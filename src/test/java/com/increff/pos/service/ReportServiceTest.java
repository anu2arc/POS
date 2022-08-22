package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.InventoryReportData;
import com.increff.pos.model.Data.SalesReportData;
import com.increff.pos.model.Form.ReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportServiceTest extends AbstractUnitTest {
    @Autowired
    private ReportService reportService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    private BrandPojo createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandPojo createBrand(String brand, String category){
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    private ProductPojo createProduct(){
        return createProduct(1,"airmax",1000.0,"b1");
    }
    private ProductPojo createProduct(int brandCategory,String name,double mrp,String barcode){
        ProductPojo productPojo=new ProductPojo();
        productPojo.setBrandCategory(brandCategory);
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        return productPojo;
    }

    private InventoryPojo createInventory(){
        return createInventory(1,100);
    }

    private InventoryPojo createInventory(int id,int quantity){
        InventoryPojo inventoryPojo=new InventoryPojo();
        inventoryPojo.setId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    private OrderItemPojo createOrder(){
        return createOrder(1,1,100,1);
    }

    private OrderItemPojo createOrder(int orderID,int quantity,double sellingPrice,int productId) {
        OrderItemPojo orderItemPojo=new OrderItemPojo();
        orderItemPojo.setOrderId(orderID);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
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
    @Test
    public void testSalesReport() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        productService.add(createProduct(brandPojo.getId(),"airmax",1000.0,"b1"));
        inventoryService.add((createInventory(productService.getByBarcode("b1").getId(),200)));
        orderService.add();
        List<OrderItemPojo> list=new ArrayList<>();
        list.add(createOrder(orderService.getAll().get(0).getId(),1,100,productService.getByBarcode("b1").getId()));
        orderItemService.add(list);
        List<SalesReportData> salesReportData=reportService.salesReport(createForm());
        assertEquals(1,salesReportData.size());
    }

    @Test
    public void testSalesReport1() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        productService.add(createProduct(brandPojo.getId(),"airmax",1000.0,"b1"));
        productService.add(createProduct(brandPojo.getId(),"shoes",2000.0,"b2"));
        inventoryService.add((createInventory(productService.getByBarcode("b1").getId(),200)));
        inventoryService.add((createInventory(productService.getByBarcode("b2").getId(),100)));
        orderService.add();
        List<OrderItemPojo> list=new ArrayList<>();
        list.add(createOrder(orderService.getAll().get(0).getId(),1,100,productService.getByBarcode("b1").getId()));
        list.add(createOrder(orderService.getAll().get(0).getId(),2,200,productService.getByBarcode("b2").getId()));
        orderItemService.add(list);
        List<SalesReportData> salesReportData=reportService.salesReport(createForm());
        assertEquals(1,salesReportData.size());

        assertEquals(3,(int) salesReportData.get(0).getQuantity());
        assertEquals(500.0, salesReportData.get(0).getRevenue(), 0.0);
    }

    @Test
    public void testInventoryReport() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        productService.add(createProduct(brandPojo.getId(),"airmax",1000.0,"b1"));
        inventoryService.add((createInventory(productService.getByBarcode("b1").getId(),100)));
        List<InventoryReportData> list=reportService.inventoryReport();
        assertEquals(1,list.size());
        assertEquals("nike",list.get(0).getBrand());
        assertEquals("shoes",list.get(0).getCategory());
        assertEquals(100, (int) list.get(0).getQuantity());
    }

    @Test
    public void testInventoryReport1() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        productService.add(createProduct(brandPojo.getId(),"airmax",1000.0,"b1"));
        productService.add(createProduct(brandPojo.getId(),"shoes",2000.0,"b2"));
        inventoryService.add((createInventory(productService.getByBarcode("b1").getId(),200)));
        inventoryService.add((createInventory(productService.getByBarcode("b2").getId(),100)));

        List<InventoryReportData> list=reportService.inventoryReport();
        assertEquals(1,list.size());
        assertEquals("nike",list.get(0).getBrand());
        assertEquals("shoes",list.get(0).getCategory());
        assertEquals(300, (int) list.get(0).getQuantity());
    }
}
