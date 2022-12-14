package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao {
    private static final String SELECT_ID = "select p from BrandPojo p where id=:id";
    private static final String SELECT_ALL = "select p from BrandPojo p";
    private static final String IS_PRESENT_BRAND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";
    @PersistenceContext
    private EntityManager entityManager;
    private TypedQuery<BrandPojo> getQuery(String jpql) {
        return entityManager.createQuery(jpql, BrandPojo.class);
    }
    @Transactional
    public BrandPojo insert(BrandPojo brandPojo) {
        entityManager.persist(brandPojo);
        return brandPojo;
    }

    public BrandPojo setIsPresent(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(IS_PRESENT_BRAND_CATEGORY);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return query.getSingleResult();
    }
    public BrandPojo select(Integer id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }
}
