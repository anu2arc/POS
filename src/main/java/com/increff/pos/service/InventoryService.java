package com.increff.pos.service;

import com.increff.pos.Util.InventoryUtil;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryUtil inventoryUtil;
    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) {
        inventoryDao.insert(inventoryPojo);
    }
    public void bulkAdd(List<InventoryPojo> itemList) throws ApiException {
        for (InventoryPojo item : itemList) {
            try {
                get(item.getId());
                update(item.getId(), item);
            } catch (ApiException exception) {
                add(item);
            }
        }
    }
    @Transactional
    public void delete(Integer id) { inventoryDao.delete(id); }
    public InventoryPojo get(Integer id) throws ApiException {
        try{
            return inventoryDao.select(id);
        }
        catch (Exception exception){
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
    }
    @Transactional
    public List<InventoryPojo> getAll() { return inventoryDao.selectAll();
    }
    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo pojo = get(id);
        pojo.setQuantity(inventoryPojo.getQuantity());
    }
    @Transactional(rollbackOn = ApiException.class)
    public void orderPlace(Integer id,Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo=get(id);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity()-quantity);
    }
}
