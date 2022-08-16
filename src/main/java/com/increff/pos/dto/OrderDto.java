package com.increff.pos.dto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDto {
    @Autowired
    private OrderService service;

    public OrderData add(){
        return DtoHelper.convert(service.add());
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list=service.getAll();
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }

    public OrderData getById(Integer id) throws ApiException {
        return DtoHelper.convert(service.get(id));
    }
    public void delete(Integer id) {
        service.delete(id);
    }

    public List<OrderData> getByRange(OrderForm f) {
        List<OrderPojo> list=service.getByRange(f.getStartDate(),f.getEndDate());
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }
}
