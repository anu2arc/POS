package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void testAdd(){
        orderService.add();
    }

    @Test
    public void testGet() throws ApiException {
        OrderPojo orderPojo= orderService.add();
        OrderPojo pojo=orderService.get(orderPojo.getId());
        assertEquals(pojo.getId(),orderPojo.getId());
        assertEquals(pojo.getTime(),orderPojo.getTime());
    }

    @Test
    public void testDelete() throws ApiException {
        OrderPojo orderPojo= orderService.add();
        orderService.delete(orderPojo.getId());
        List<OrderPojo> list=orderService.getAll();
        assertEquals(0,list.size());
    }

    @Test
    public void testGetWithWrongID(){
        try{
            orderService.get(0);
        }
        catch (ApiException exception){
            assertEquals("Order with given ID does not exit, id: 0",exception.getMessage().trim());
        }
    }

    @Test
    public void testGetAll(){
        List<OrderPojo> orderPojoList=new ArrayList<>();
        orderPojoList.add(orderService.add());
        orderPojoList.add(orderService.add());

        List<OrderPojo> pojoList=orderService.getAll();

        assertEquals(2, pojoList.size());

        assertEquals(orderPojoList.get(0).getId(),pojoList.get(0).getId());
        assertEquals(orderPojoList.get(0).getTime(),pojoList.get(0).getTime());

        assertEquals(orderPojoList.get(1).getId(),pojoList.get(1).getId());
        assertEquals(orderPojoList.get(1).getTime(),pojoList.get(1).getTime());
    }

    @Test
    public void testGetByRange(){
        List<OrderPojo> orderPojoList=new ArrayList<>();
        orderPojoList.add(orderService.add());
        orderPojoList.add(orderService.add());

        List<OrderPojo> pojoList=orderService.getByRange(orderPojoList.get(0).getTime(),orderPojoList.get(1).getTime());

        assertEquals(2, pojoList.size());

        assertEquals(orderPojoList.get(0).getId(),pojoList.get(0).getId());
        assertEquals(orderPojoList.get(0).getTime(),pojoList.get(0).getTime());

        assertEquals(orderPojoList.get(1).getId(),pojoList.get(1).getId());
        assertEquals(orderPojoList.get(1).getTime(),pojoList.get(1).getTime());
    }
}
