package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.ProductData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.model.Form.ProductForm;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDto brandDto;

    private ProductForm createProduct(){
        return createProduct("nike","shoes","airmax",1000.0,"b1");
    }
    private ProductForm createProduct(String brand, String category, String name, double mrp, String barcode){
        ProductForm productForm=new ProductForm();
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);
        productForm.setBarcode(barcode);
        return productForm;
    }

    private BrandForm createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandForm createBrand(String brand, String category){
        BrandForm brandForm=new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    @Test
    public void testAdd() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
    }

    @Test
    public void testAdd1() throws Exception {
        try{
            productDto.add(createProduct());
        }
        catch (ApiException exception){
            assertEquals("Invalid Brand Category pair",exception.getMessage().trim());
        }
    }

    @Test
    public void testAdd2() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        try{
            productDto.add(createProduct());
        }
        catch (ApiException exception){
            assertEquals("Barcode already exist",exception.getMessage().trim());
        }
    }

    @Test
    public void testBulkAdd() throws Exception {
        brandDto.add(createBrand());
        List<ProductForm> productFromList=new ArrayList<>();
        productFromList.add(createProduct());
        productFromList.add((createProduct("nike","shoes","jordan",2000.0,"b2")));
        productDto.bulkAdd(productFromList);
    }

    @Test
    public void testBulkAdd1() throws Exception {
        brandDto.add(createBrand());
        List<ProductForm> productFromList=new ArrayList<>();
        productFromList.add(createProduct());
        productFromList.add((createProduct("nike","shoes","jordan",2000.0,"b1")));
        try{
            productDto.bulkAdd(productFromList);
        }
        catch (ApiException exception){
            assertEquals("2: Duplicate barcode",exception.getMessage().trim());
        }
    }

    @Test
    public void testBulkAdd2() throws Exception {
        List<ProductForm> productFromList=new ArrayList<>();
        productFromList.add(createProduct());
        try{
            productDto.bulkAdd(productFromList);
        }
        catch (ApiException exception){
            assertEquals("1: Invalid Brand Category pair",exception.getMessage().trim());
        }
    }

    @Test
    public void testBulkAdd3() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        List<ProductForm> productFromList=new ArrayList<>();
        productFromList.add((createProduct("nike","shoes","jordan",2000.0,"b1")));
        try{
            productDto.bulkAdd(productFromList);
        }
        catch (ApiException exception){
            assertEquals("1: Barcode already exist",exception.getMessage().trim());
        }
    }

    @Test
    public void testGetAll() throws ApiException {
        brandDto.add(createBrand());
        List<ProductForm> productFromList=new ArrayList<>();
        productFromList.add(createProduct());
        productFromList.add((createProduct("nike","shoes","jordan",2000.0,"b2")));
        productDto.bulkAdd(productFromList);

        List<ProductData> dataList=productDto.getAll();

        assertEquals(productFromList.size(),dataList.size());

        assertEquals(productFromList.get(0).getBarcode(),dataList.get(0).getBarcode());
        assertEquals(productFromList.get(0).getName(),dataList.get(0).getName());

        assertEquals(productFromList.get(1).getBarcode(),dataList.get(1).getBarcode());
        assertEquals(productFromList.get(1).getName(),dataList.get(1).getName());
    }

    @Test
    public void testGet() throws Exception {
        brandDto.add(createBrand());
        ProductForm productFrom=createProduct();
        productDto.add(productFrom);
        ProductData data=productDto.getAll().get(0);
        ProductData productData=productDto.getById(data.getId());
        assertEquals(productData.getBarcode(),productFrom.getBarcode());
        assertEquals(productData.getName(),productFrom.getName());
        assertEquals(productData.getMrp(),productFrom.getMrp());
    }


    @Test
    public void testGetByBarcode() throws Exception {
        brandDto.add(createBrand());
        ProductForm productFrom=createProduct();
        productDto.add(productFrom);
        ProductData productData=productDto.getByBarcode(productFrom.getBarcode());

        assertEquals(productData.getBarcode(),productFrom.getBarcode());
        assertEquals(productData.getName(),productFrom.getName());
        assertEquals(productData.getMrp(),productFrom.getMrp());
    }

    @Test
    public void testGetByBarcode1() throws Exception {
        try{
            productDto.getByBarcode("");
        }
        catch (ApiException exception){
            assertEquals("Barcode cannot be empty",exception.getMessage().trim());
        }
    }

    @Test
    public void testUpdate() throws Exception {
        brandDto.add(createBrand());
        ProductForm productFrom=createProduct();
        productDto.add(productFrom);
        productFrom.setName("newShoes");
        productDto.update(productFrom.getBarcode(),productFrom);
        ProductData form=productDto.getByBarcode(productFrom.getBarcode());
        assertEquals(productFrom.getName(),form.getName());
    }

    @Test
    public void testUpdate1() throws Exception {
        brandDto.add(createBrand());
        ProductForm productFrom=createProduct();
        productDto.add(productFrom);
        productFrom.setBrand("alpha");
        try{
            productDto.update(productFrom.getBarcode(),productFrom);
        }
        catch (ApiException exception){
            assertEquals("please provide a valid brand category pair",exception.getMessage().trim());
        }
    }
}
