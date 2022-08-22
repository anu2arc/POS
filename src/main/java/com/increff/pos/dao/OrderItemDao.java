package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao {
    private static final String SELECT_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";
    @PersistenceContext
    private EntityManager entityManager;
    private TypedQuery<OrderItemPojo> getQuery(String jpql) {
        return entityManager.createQuery(jpql, OrderItemPojo.class);
    }
    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        entityManager.persist(orderItemPojo);
    }
    public List<OrderItemPojo> selectOrder(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ORDER_ID);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
