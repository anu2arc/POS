package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDaoTest extends AbstractUnitTest {
    @Autowired
    private OrderDao orderDao;

    private OrderPojo createOrder(){
        OrderPojo orderPojo=new OrderPojo();
        orderPojo.setTime();
        return orderPojo;
    }
    @Test
    public void testInsert(){
        orderDao.insert(createOrder());
    }
    @Test
    public void testSelect(){
        OrderPojo orderPojo=createOrder();
        orderDao.insert(orderPojo);
        OrderPojo pojo=orderDao.select(orderPojo.getId());
        assertEquals(orderPojo.getId(),pojo.getId());
        assertEquals(orderPojo.getTime(),pojo.getTime());
    }
    @Test
    public void testSelectAll(){
        OrderPojo orderPojo=createOrder();
        orderDao.insert(orderPojo);
        orderDao.insert(createOrder());
        List<OrderPojo> list=orderDao.selectAll();
        assertEquals(2,list.size());
        assertEquals(orderPojo.getId(),list.get(0).getId());
        assertEquals(orderPojo.getTime(),list.get(0).getTime());
    }

    @Test
    public void testDelete(){
        OrderPojo orderPojo=createOrder();
        orderDao.insert(orderPojo);
        List<OrderPojo> list=orderDao.selectAll();
        assertEquals(1,list.size());
        orderDao.delete(orderPojo.getId());
        list=orderDao.selectAll();
        assertEquals(0,list.size());
    }
    @Test
    public void testGetByRange(){
        OrderPojo orderPojo1=createOrder();
        OrderPojo orderPojo2=createOrder();
        orderDao.insert(orderPojo1);
        orderDao.insert(orderPojo2);
        List<OrderPojo> list=orderDao.getByRange(orderPojo1.getTime(),orderPojo2.getTime());
        assertEquals(2,list.size());
        assertEquals(orderPojo1.getId(),list.get(0).getId());
        assertEquals(orderPojo1.getTime(),list.get(0).getTime());
    }
}
