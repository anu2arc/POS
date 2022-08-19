package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDaoTest extends AbstractUnitTest {
    @Autowired
    private BrandDao brandDao;

    private BrandPojo createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandPojo createBrand(String brand, String category){
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    @Test
    public void testInsert(){
        brandDao.insert(createBrand());
    }

    @Test
    public void testSetIsPresent(){
        BrandPojo brandPojo=createBrand();
        brandDao.insert(brandPojo);
        BrandPojo pojo=brandDao.setIsPresent(brandPojo.getBrand(),brandPojo.getCategory());
        assertEquals(brandPojo.getId(),pojo.getId());
    }

    @Test
    public void testSelect(){
        BrandPojo brandPojo=createBrand();
        brandDao.insert(brandPojo);
        BrandPojo pojo=brandDao.select(brandPojo.getId());
        assertEquals(brandPojo.getBrand(),pojo.getBrand());
        assertEquals(brandPojo.getCategory(),pojo.getCategory());
    }

    @Test
    public void testSelectAll(){
        brandDao.insert(createBrand());
        brandDao.insert(createBrand("test1","category"));

        List<BrandPojo> list=brandDao.selectAll();

        assertEquals(2,list.size());
    }
}
