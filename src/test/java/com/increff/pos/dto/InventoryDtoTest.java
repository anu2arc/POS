package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.InventoryData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.model.Form.ProductFrom;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    private BrandForm createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandForm createBrand(String brand, String category){
        BrandForm brandForm=new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
    private ProductFrom createProduct(){
        return createProduct("nike","shoes","airmax",1000.0,"test1");
    }
    private ProductFrom createProduct(String brand,String category,String name,double mrp,String barcode){
        ProductFrom productForm=new ProductFrom();
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setName(name);
        productForm.setMrp(mrp);
        productForm.setBarcode(barcode);
        return productForm;
    }
    private InventoryForm createInventory(){
        return createInventory("test1",100);
    }
    private InventoryForm createInventory(String barcode,int quantity){
        InventoryForm inventoryForm=new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    @Test
    public void testAdd() throws Exception {
        inventoryDto.add(1,createInventory());
    }

    @Test
    public void testAdd1() throws Exception {
        inventoryDto.add(1,createInventory());
        inventoryDto.add(1,createInventory("test1",200));
    }

    @Test
    public void testBulkAdd() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        productDto.add(createProduct("nike","shoes","jordan",2000.0,"test2"));
        List<InventoryForm> inventoryFormList=new ArrayList<>();
        inventoryFormList.add(createInventory());
        inventoryFormList.add(createInventory("test2",200));
        inventoryDto.bulkAdd(inventoryFormList);
    }

    @Test
    public void testBulkAdd1() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        productDto.add(createProduct("nike","shoes","jordan",2000.0,"test2"));
        List<InventoryForm> inventoryFormList=new ArrayList<>();
        inventoryFormList.add(createInventory());
        inventoryFormList.add(createInventory("t2",200));
        try{
            inventoryDto.bulkAdd(inventoryFormList);
        }
        catch (ApiException exception){
            assertEquals("2: Product with given barcode does not exit, barcode: t2",exception.getMessage().trim());
        }
    }

    @Test
    public void testGetAll() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        productDto.add(createProduct("nike","shoes","jordan",2000.0,"test2"));
        List<InventoryForm> inventoryFormList=new ArrayList<>();
        inventoryFormList.add(createInventory());
        inventoryFormList.add(createInventory("test2",200));
        inventoryDto.bulkAdd(inventoryFormList);

        List<InventoryData> inventoryData=inventoryDto.getAll();

        assertEquals(inventoryData.size(),inventoryFormList.size());

        assertEquals(inventoryData.get(0).getQuantity(),inventoryFormList.get(0).getQuantity());
        assertEquals(inventoryData.get(1).getQuantity(),inventoryFormList.get(1).getQuantity());
    }

    @Test
    public void testUpdate() throws Exception {
        InventoryForm inventoryForm=createInventory();
        inventoryDto.add(1,inventoryForm);
        inventoryForm.setQuantity(200);
        inventoryDto.update(1,inventoryForm);

        InventoryData form=inventoryDto.getAll().get(0);

        assertEquals(inventoryForm.getQuantity(),form.getQuantity());
    }

    @Test
    public void testGetById() throws Exception {
        InventoryForm inventoryForm=createInventory();
        inventoryDto.add(1,inventoryForm);
        InventoryData form=inventoryDto.getById(1);
        assertEquals(inventoryForm.getQuantity(),form.getQuantity());
    }

    @Test
    public void testGetByBarcode() throws Exception {
        brandDto.add(createBrand());
        productDto.add(createProduct());
        InventoryForm inventoryForm=createInventory();
        inventoryDto.add(productDto.getAll().get(0).getId(),inventoryForm);
        InventoryData byBarcode=inventoryDto.getByBarcode(inventoryForm.getBarcode());
        InventoryData data=inventoryDto.getAll().get(0);
        assertEquals(data.getQuantity(),byBarcode.getQuantity());
    }
}
