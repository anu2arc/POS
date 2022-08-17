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

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) {
        inventoryDao.insert(inventoryPojo);
    }
    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<InventoryPojo> itemList) throws ApiException {
        for(InventoryPojo item:itemList){
            try{
                getCheck(item.getId());
                update(item.getId(),item);
            }
            catch (ApiException exception){
                add(item);
            }
        }
    }

//    private void insert(InventoryPojo inventoryPojo){
//        try{
//            InventoryPojo temp=getCheck(inventoryPojo.getId());
//            temp.setQuantity(inventoryPojo.getQuantity());
//        }
//        catch (Exception e){
//            inventoryDao.insert(inventoryPojo);
//        }
//    }

    @Transactional
    public void delete(Integer id) {
        inventoryDao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo pojo = getCheck(id);
        pojo.setQuantity(inventoryPojo.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public void orderPlace(Integer id,Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo=getCheck(id);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity()-quantity);
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) throws ApiException {
        try{
            return inventoryDao.select(id);
        }
        catch (Exception exception){
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
    }
}
