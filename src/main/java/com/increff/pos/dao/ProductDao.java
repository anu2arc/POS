package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao {
    private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
    private static final String SELECT_ALL = "select p from ProductPojo p";
    private static final String DELETE_ID = "delete from ProductPojo p where id=:id";
    private static final String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ProductPojo insert(ProductPojo productPojo){
        entityManager.persist(productPojo);
        return productPojo;
    }

    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public ProductPojo selectBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public Integer delete(Integer id) {
        Query query = entityManager.createQuery(DELETE_ID);
        query.setParameter("id", id);
        return query.executeUpdate();
    }
    TypedQuery<ProductPojo> getQuery(String jpql) {
        return entityManager.createQuery(jpql, ProductPojo.class);
    }

}
