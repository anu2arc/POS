package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.OrderForm;
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
    public void testGetByRange() throws ApiException {
        List<OrderData> orderDataList=new ArrayList<>();
        orderDataList.add(orderDto.add());
        orderDataList.add(orderDto.add());

        OrderForm orderForm=new OrderForm();
        orderForm.setStartDate(orderDataList.get(0).getTime().toString());
        orderForm.setEndDate(orderDataList.get(1).getTime().toString());

        List<OrderData> data=orderDto.getByRange(orderForm);

        assertEquals(data.size(),orderDataList.size());

        assertEquals(data.get(0).getTime(),orderDataList.get(0).getTime());
        assertEquals(data.get(0).getId(),orderDataList.get(0).getId());

        assertEquals(data.get(1).getTime(),orderDataList.get(1).getTime());
        assertEquals(data.get(1).getId(),orderDataList.get(1).getId());

    }
}
