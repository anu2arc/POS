package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    private BrandService brandService;

    @Test
    public void testAdd(){
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrand("Nike");
        brandPojo.setCategory("    Shoes");
        brandService.add(brandPojo);
    }

    @Test
    public void testBulkAdd(){

    }

    @Test
    public void testDelete(){

    }
}
