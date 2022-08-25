package com.increff.pos.dto;
import com.increff.pos.Util.OrderUtil;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDto {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderUtil orderUtil;

    public OrderData add(){
        return DtoHelper.convert(orderService.add());
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list= orderService.getAll();
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo orderPojo:list){
            orderDataList.add(DtoHelper.convert(orderPojo));
        }
        return orderDataList;
    }

    public OrderData getById(Integer id) throws ApiException {
        return DtoHelper.convert(orderService.get(id));
    }

    public List<OrderData> getByRange(OrderForm form) throws ApiException {
        ZonedDateTime start=orderUtil.convert(form.getStartDate());
        ZonedDateTime end=orderUtil.convert(form.getStartDate());
        orderUtil.validate(start,end);
        List<OrderPojo> list= orderService.getByRange(start,end);
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }
}
