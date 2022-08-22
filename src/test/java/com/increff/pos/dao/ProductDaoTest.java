package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends AbstractUnitTest {
    @Autowired
    private ProductDao productDao;

    private ProductPojo createProduct(){
        return createProduct(1,"airmax",1000.0,"b1");
    }
    private ProductPojo createProduct(int brandCategory,String name,double mrp,String barcode){
        ProductPojo productPojo=new ProductPojo();
        productPojo.setBrandCategory(brandCategory);
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        return productPojo;
    }

    @Test
    public void testInsert(){
        productDao.insert(createProduct());
    }

    @Test
    public void testSelect(){
        ProductPojo productPojo=createProduct();
        productDao.insert(productPojo);
        ProductPojo pojo=productDao.select(productPojo.getId());

        assertEquals(productPojo.getBarcode(),pojo.getBarcode());
        assertEquals(productPojo.getName(),pojo.getName());
        assertEquals(productPojo.getBrandCategory(),pojo.getBrandCategory());
        assertEquals(productPojo.getMrp(),pojo.getMrp());
    }

    @Test
    public void testSelectBarcode(){
        ProductPojo productPojo=createProduct();
        productDao.insert(productPojo);
        ProductPojo pojo=productDao.selectBarcode(productPojo.getBarcode());

        assertEquals(productPojo.getId(),pojo.getId());
        assertEquals(productPojo.getName(),pojo.getName());
        assertEquals(productPojo.getBrandCategory(),pojo.getBrandCategory());
        assertEquals(productPojo.getMrp(),pojo.getMrp());
    }

    @Test
    public void testSelectAll(){
        List<ProductPojo> productPojoList=new ArrayList<>();
        productPojoList.add(createProduct());
        productPojoList.add(createProduct(2,"jordan",2000.0,"b2"));
        productDao.insert(productPojoList.get(0));
        productDao.insert(productPojoList.get(1));
        List<ProductPojo> pojoList= productDao.selectAll();

        assertEquals(productPojoList.size(),pojoList.size());

        assertEquals(productPojoList.get(0).getBarcode(),pojoList.get(0).getBarcode());
        assertEquals(productPojoList.get(0).getName(),pojoList.get(0).getName());
        assertEquals(productPojoList.get(0).getBrandCategory(),pojoList.get(0).getBrandCategory());
        assertEquals(productPojoList.get(0).getMrp(),pojoList.get(0).getMrp());
    }
}
