package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    private InventoryService inventoryService;

    private InventoryPojo createInventory(){
        return createInventory(1,100);
    }

    private InventoryPojo createInventory(int id,int quantity){
        InventoryPojo inventoryPojo=new InventoryPojo();
        inventoryPojo.setId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    @Test
    public void testAdd(){
        InventoryPojo inventoryPojo=createInventory();
        inventoryService.add(inventoryPojo);
    }

    @Test
    public void testGetAll(){
        InventoryPojo inventoryPojo=createInventory();
        inventoryService.add(inventoryPojo);
        List<InventoryPojo> inventoryPojoList=inventoryService.getAll();

        assertEquals(inventoryPojo.getId(),inventoryPojoList.get(0).getId());
        assertEquals(inventoryPojo.getQuantity(),inventoryPojoList.get(0).getQuantity());
    }

    @Test
    public void testBulkAdd() throws ApiException {
        List<InventoryPojo> list=new ArrayList<>();
        list.add(createInventory());
        list.add(createInventory(2,100));
        inventoryService.bulkAdd(list);

        List<InventoryPojo> inventoryPojoList=inventoryService.getAll();
        assertEquals(inventoryPojoList.get(0).getId(),inventoryPojoList.get(0).getId());
        assertEquals(inventoryPojoList.get(0).getQuantity(),inventoryPojoList.get(0).getQuantity());

        assertEquals(inventoryPojoList.get(1).getId(),inventoryPojoList.get(1).getId());
        assertEquals(inventoryPojoList.get(1).getQuantity(),inventoryPojoList.get(1).getQuantity());
    }

    @Test
    public void testGet() throws ApiException {
        InventoryPojo inventoryPojo=createInventory();
        inventoryService.add(inventoryPojo);
        InventoryPojo pojo=inventoryService.get(1);
        assertEquals(inventoryPojo.getId(),pojo.getId());
        assertEquals(inventoryPojo.getQuantity(),pojo.getQuantity());
    }

    @Test
    public void testGetWithWrongId() throws ApiException {
        try{
            inventoryService.get(0);
        }
        catch(ApiException exception){
            assertEquals("Inventory with given ID does not exit, id: 0",exception.getMessage().trim());
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        InventoryPojo inventoryPojo=createInventory();
        inventoryService.add(inventoryPojo);
        inventoryPojo.setQuantity(200);

        InventoryPojo pojo=inventoryService.get(1);
        assertEquals(inventoryPojo.getQuantity(),pojo.getQuantity());
    }

    @Test
    public void testBulkAddWithUpdate() throws ApiException {
        List<InventoryPojo> list=new ArrayList<>();
        list.add(createInventory());
        list.add(createInventory(1,200));
        inventoryService.bulkAdd(list);

        List<InventoryPojo> inventoryPojoList=inventoryService.getAll();
        assertEquals(inventoryPojoList.get(0).getId(),inventoryPojoList.get(0).getId());
        assertEquals(inventoryPojoList.get(0).getQuantity(),inventoryPojoList.get(0).getQuantity());
    }

    @Test
    public void testOrderPlace() throws ApiException {
        InventoryPojo inventoryPojo=createInventory();
        inventoryService.add(inventoryPojo);
        inventoryService.orderPlace(inventoryPojo.getId(),10);
        InventoryPojo pojo=inventoryService.get(inventoryPojo.getId());
        assertEquals(90, (int) pojo.getQuantity());
    }
}
