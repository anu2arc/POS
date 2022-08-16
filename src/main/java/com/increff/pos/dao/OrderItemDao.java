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
    private EntityManager em;

    @Transactional
    public void insert(OrderItemPojo p) {
        em.persist(p);
    }

    public List<OrderItemPojo> selectorder(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ORDER_ID);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
    TypedQuery<OrderItemPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderItemPojo.class);
    }
}
