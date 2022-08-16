package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao {
    private static final String SELECT_ID = "select p from OrderPojo p where id=:id";
    private static final String SELECT_ALL = "select p from OrderPojo p";
    private static final String DELETE_ID = "delete from OrderPojo p where id=:id";
    private static final String SELECT_FOR_DATE_RANGE = "select p from OrderPojo p where time >= :startDate and time <= :endDate";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public OrderPojo insert(OrderPojo p) {
        em.persist(p);
        return p;
    }

    public OrderPojo select(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ID);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public Integer delete(Integer id) {
        Query query = em.createQuery(DELETE_ID);
        query.setParameter("id", id);
        return query.executeUpdate();
    }
    public List<OrderPojo> getByRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        Query query = em.createQuery(SELECT_FOR_DATE_RANGE);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<OrderPojo> list= query.getResultList();
        return list;
    }
    TypedQuery<OrderPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderPojo.class);
    }


}
