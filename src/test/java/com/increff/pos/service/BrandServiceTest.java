package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    private BrandService brandService;

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
    public void testAdd(){
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
    }

    @Test
    public void testGetWithWrongID(){
        try{
            brandService.get(0);
        } catch (ApiException exception) {
            assertEquals("Brand with given ID does not exit, id: 0",exception.getMessage().trim());
        }
    }

    @Test
    public void testBulkAdd() throws ApiException {
        List<BrandPojo> brandPojoList=new ArrayList<>();
        brandPojoList.add(createBrand());
        brandPojoList.add(createBrand("alpha","beta"));
        brandService.bulkAdd(brandPojoList);
        brandPojoList=brandService.getAll();
        assertEquals(2,brandPojoList.size());
        assertEquals("nike",brandPojoList.get(0).getBrand());
        assertEquals("shoes",brandPojoList.get(0).getCategory());

        assertEquals("alpha",brandPojoList.get(1).getBrand());
        assertEquals("beta",brandPojoList.get(1).getCategory());
    }
    @Test
    public void testBulkAddWithDuplicates() {
        List<BrandPojo> brandPojoList=new ArrayList<>();
        brandPojoList.add(createBrand());
        brandPojoList.add(createBrand());
        try{
            brandService.bulkAdd(brandPojoList);
        }
        catch (Exception exception){
            assertEquals("2: Brand Category pair already exist",exception.getMessage().trim());
        }
    }

    @Test
    public void testGet() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        brandPojo=brandService.get(brandPojo.getId());
        assertEquals("nike",brandPojo.getBrand());
        assertEquals("shoes",brandPojo.getCategory());
    }
    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add((brandPojo));
        brandPojo.setCategory("bags");
        brandService.update(brandPojo);
        brandPojo=brandService.get(brandPojo.getId());
        assertEquals("nike",brandPojo.getBrand());
        assertEquals("bags",brandPojo.getCategory());
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        List<BrandPojo> list=brandService.getAll();
        for(BrandPojo bp:list){
            System.out.println(bp.getBrand());
        }
        assertEquals(1,list.size());
        assertEquals("nike",list.get(0).getBrand());
        assertEquals("shoes",list.get(0).getCategory());
    }

    @Test
    public void testCheckPair(){
        BrandPojo brandPojo=createBrand();
        brandService.add(brandPojo);
        assertEquals(brandPojo.getId(),brandService.checkPair(brandPojo.getBrand(), brandPojo.getCategory()).getId());
    }
}
