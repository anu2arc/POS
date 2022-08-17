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
    private OrderService orderService;

    public OrderData add(){
        return DtoHelper.convert(orderService.add());
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list= orderService.getAll();
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }

    public OrderData getById(Integer id) throws ApiException {
        return DtoHelper.convert(orderService.get(id));
    }
    public void delete(Integer id) {
        orderService.delete(id);
    }

    public List<OrderData> getByRange(OrderForm f) {
        List<OrderPojo> list= orderService.getByRange(f.getStartDate(),f.getEndDate());
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }
}
