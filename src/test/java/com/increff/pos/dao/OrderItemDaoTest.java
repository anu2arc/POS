package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDaoTest extends AbstractUnitTest {
    @Autowired
    private OrderItemDao orderItemDao;

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

    @Test
    public void testInsert(){
        orderItemDao.insert(createOrder());
    }
    @Test
    public void testSelectOrder(){
        OrderItemPojo o1=createOrder();
        OrderItemPojo o2=createOrder(1,2,200,2);
        orderItemDao.insert(o1);
        orderItemDao.insert(o2);
        List<OrderItemPojo> list=orderItemDao.selectOrder(1);
        assertEquals(2,list.size());

        assertEquals(o1.getProductId(),list.get(0).getProductId());
        assertEquals(o1.getQuantity(),list.get(0).getQuantity());
        assertEquals(o1.getSellingPrice(),list.get(0).getSellingPrice());
        assertEquals(o1.getOrderId(),list.get(0).getOrderId());

        assertEquals(o2.getProductId(),list.get(1).getProductId());
        assertEquals(o2.getQuantity(),list.get(1).getQuantity());
        assertEquals(o2.getSellingPrice(),list.get(1).getSellingPrice());
        assertEquals(o2.getOrderId(),list.get(1).getOrderId());
    }
}
