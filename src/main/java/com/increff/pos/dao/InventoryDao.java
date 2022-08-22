package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static final String SELECT_ALL = "select p from InventoryPojo p";
    private TypedQuery<InventoryPojo> getQuery(String jpql) {
        return entityManager.createQuery(jpql, InventoryPojo.class);
    }

    @Transactional
    public void insert(InventoryPojo inventoryPojo){
        entityManager.persist(inventoryPojo);
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
}
