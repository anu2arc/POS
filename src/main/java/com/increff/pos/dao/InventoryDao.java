package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao {
    private static final String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static final String SELECT_ALL = "select p from InventoryPojo p";
    private static final String DELETE_ID = "delete from InventoryPojo p where id=:id";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(InventoryPojo p){
        em.persist(p);
    }

    public InventoryPojo select(Integer id) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public Integer delete(Integer id) {
        Query query = em.createQuery(DELETE_ID);
        query.setParameter("id", id);
        return query.executeUpdate();
    }
    TypedQuery<InventoryPojo> getQuery(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }
}
