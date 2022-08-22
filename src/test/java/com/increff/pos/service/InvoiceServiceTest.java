//package com.increff.pos.service;
//
//import com.increff.pos.AbstractUnitTest;
//import com.increff.pos.pojo.InventoryPojo;
//import com.increff.pos.pojo.OrderItemPojo;
//import com.increff.pos.pojo.OrderPojo;
//import org.hibernate.criterion.Order;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.transform.TransformerException;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
//public class InvoiceServiceTest extends AbstractUnitTest {
//    @Autowired
//    private InvoiceService invoiceService;
//    @Autowired
//    private OrderItemService orderItemService;
//    @Autowired
//    private InventoryService inventoryService;
//    @Autowired
//    private OrderService orderService;
//    private OrderItemPojo createOrder(){
//        return createOrder(1,1,100,1);
//    }
//
//    private OrderItemPojo createOrder(int orderID,int quantity,double sellingPrice,int productId) {
//        OrderItemPojo orderItemPojo=new OrderItemPojo();
//        orderItemPojo.setOrderId(orderID);
//        orderItemPojo.setQuantity(quantity);
//        orderItemPojo.setSellingPrice(sellingPrice);
//        orderItemPojo.setProductId(productId);
//        return orderItemPojo;
//    }
//
//    private InventoryPojo createInventory(int id, int quantity){
//        InventoryPojo inventoryPojo=new InventoryPojo();
//        inventoryPojo.setId(id);
//        inventoryPojo.setQuantity(quantity);
//        return inventoryPojo;
//    }
//
////    @Test
////    public void testGetOrderInvoice() throws ApiException, IOException, TransformerException {
////        InventoryPojo inventoryPojo=createInventory(1,100);
////        inventoryService.add(inventoryPojo);
////        OrderPojo orderPojo=orderService.add();
////        List<OrderItemPojo> list=new ArrayList<>();
////        list.add(createOrder(orderPojo.getId(),1,100,1));
////        orderItemService.add(list);
////
////        invoiceService.getOrderInvoice(,orderPojo.getId());
////        File file=new File("src/main/resources/Invoice/invoice1.pdf");
////        assertTrue(file.exists());
////    }
//}
