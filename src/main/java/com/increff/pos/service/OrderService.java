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
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo add() {
        OrderPojo orderPojo=new OrderPojo();
        orderPojo.setTime();
        orderDao.insert(orderPojo);
        return orderPojo;
    }

    @Transactional
    public void delete(Integer id) {
        orderDao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }
    @Transactional
    public OrderPojo getCheck(Integer id) throws ApiException {
        try{
            return orderDao.select(id);
        }
        catch (Exception exception){
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
    }
    @Transactional
    public List<OrderPojo> getByRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.getByRange(startDate,endDate);
    }
}
