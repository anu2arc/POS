package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemServiceTest extends AbstractUnitTest {

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

    private InventoryPojo createInventory(int id, int quantity){
        InventoryPojo inventoryPojo=new InventoryPojo();
        inventoryPojo.setId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testAdd() throws ApiException {
        InventoryPojo inventoryPojo=createInventory(1,100);
        inventoryService.add(inventoryPojo);

        List<OrderItemPojo> list=new ArrayList<>();
        list.add(createOrder());
        orderItemService.add(list);
    }

    @Test
    public void testGetOrder() throws ApiException {
        InventoryPojo inventoryPojo=createInventory(1,100);
        inventoryService.add(inventoryPojo);

        List<OrderItemPojo> list=new ArrayList<>();
        list.add(createOrder());
        orderItemService.add(list);

        List<OrderItemPojo> itemPojoList=orderItemService.getOrder(1);

        assertEquals(list.get(0).getOrderId(),itemPojoList.get(0).getOrderId());
        assertEquals(list.get(0).getQuantity(),itemPojoList.get(0).getQuantity());
        assertEquals(list.get(0).getSellingPrice(),itemPojoList.get(0).getSellingPrice());
        assertEquals(list.get(0).getProductId(),itemPojoList.get(0).getProductId());
    }
}
