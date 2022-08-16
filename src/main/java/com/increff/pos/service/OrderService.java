package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo add() {
        OrderPojo p=new OrderPojo();
        p.setTime();
        dao.insert(p);
        return p;
    }

    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }
    @Transactional
    public OrderPojo getCheck(Integer id) throws ApiException {
        try{
            OrderPojo p = dao.select(id);
            return p;
        }
        catch (Exception exception){
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
    }
    @Transactional
    public List<OrderPojo> getByRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return dao.getByRange(startDate,endDate);
    }
}
