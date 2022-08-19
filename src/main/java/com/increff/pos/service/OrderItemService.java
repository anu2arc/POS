package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao dao;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for(OrderItemPojo orderItemPojo:orderItemPojoList) {
            dao.insert(orderItemPojo);
            inventoryService.orderPlace(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
    }
    @Transactional
    public List<OrderItemPojo> getOrder(Integer orderId) {
        return dao.selectOrder(orderId);
    }
}
