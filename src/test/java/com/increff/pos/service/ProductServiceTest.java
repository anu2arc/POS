package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;

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
    public void testAdd(){
        ProductPojo productPojo=createProduct();
        productService.add(productPojo);
    }

    @Test
    public void testBulkAdd() throws ApiException {
        List<ProductPojo> list=new ArrayList<>();
        list.add(createProduct());
        list.add(createProduct(1,"jordan",1000.0,"b2"));
        productService.bulkAdd(list);
        list=productService.getAll();
        assertEquals("airmax",list.get(0).getName());
        assertEquals("b1",list.get(0).getBarcode());

        assertEquals("jordan",list.get(1).getName());
        assertEquals("b2",list.get(1).getBarcode());
    }

    @Test
    public void testBulkAddWithDuplicates() throws ApiException {
        List<ProductPojo> list=new ArrayList<>();
        list.add(createProduct());
        list.add(createProduct());
        try {
            productService.bulkAdd(list);
        }
        catch (ApiException exception){
            System.out.println(exception.getMessage());
            assertEquals("2: Duplicate barcode",exception.getMessage().trim());
        }
    }

    @Test
    public void testGet() throws ApiException {
        ProductPojo productPojo=createProduct();
        productService.add(productPojo);
        ProductPojo pojo=productService.get(productPojo.getId());
        assertEquals(productPojo.getId(),pojo.getId());
    }

    @Test
    public void testGetWithWrongId(){
        try {
            productService.get(0);
        }
        catch (Exception exception){
            assertEquals("Product with given ID does not exit, id: 0",exception.getMessage().trim());
        }
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        ProductPojo productPojo=createProduct();
        productService.add(productPojo);
        ProductPojo pojo=productService.getByBarcode(productPojo.getBarcode());
        assertEquals(productPojo.getBarcode(),pojo.getBarcode());
    }

    @Test
    public void testGetByBarcodeWithWrongBarcode(){
        try {
            productService.getByBarcode("abc");
        }
        catch (Exception exception){
            assertEquals("Product with given barcode does not exit, barcode: abc",exception.getMessage().trim());
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo=createProduct();
        productService.add(productPojo);
        productPojo.setName("test");
        productService.update(productPojo.getBarcode(),productPojo);
        ProductPojo pojo= productService.getByBarcode(productPojo.getBarcode());
        assertEquals("test",pojo.getName());
    }

//    @Test
//    public void test

}
