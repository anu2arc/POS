package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDaoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDao inventoryDao;

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
    public void testInsert(){
        inventoryDao.insert(createInventory());
    }
    @Test
    public void testSelect(){
        InventoryPojo inventoryPojo=createInventory();
        inventoryDao.insert(inventoryPojo);
        InventoryPojo pojo=inventoryDao.select(inventoryPojo.getId());

        assertEquals(inventoryPojo.getQuantity(),pojo.getQuantity());
        assertEquals(inventoryPojo.getId(),pojo.getId());
    }
    @Test
    public void testSelectAll(){
        List<InventoryPojo> list=new ArrayList<>();
        list.add((createInventory()));
        list.add((createInventory(2,100)));
        inventoryDao.insert(list.get(0));
        inventoryDao.insert(list.get(1));

        List<InventoryPojo> pojoList=inventoryDao.selectAll();

        assertEquals(list.size(),pojoList.size());

        assertEquals(list.get(0).getId(),pojoList.get(0).getId());
        assertEquals(list.get(0).getQuantity(),pojoList.get(0).getQuantity());

        assertEquals(list.get(1).getId(),pojoList.get(1).getId());
        assertEquals(list.get(1).getQuantity(),pojoList.get(1).getQuantity());
    }
}
