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
    private InventoryDao dao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryUtil inventoryUtil;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) {
        dao.insert(p);
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

    private void insert(InventoryPojo p){
        try{
            InventoryPojo temp=getCheck(p.getId());
            temp.setQuantity(p.getQuantity());
        }
        catch (Exception e){
            dao.insert(p);
        }
    }

    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo p) throws ApiException {
        InventoryPojo ex = getCheck(id);
        ex.setQuantity(p.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public void orderPlace(Integer id,Integer quantity) throws ApiException {
        InventoryPojo p=getCheck(id);
        p.setQuantity(p.getQuantity()-quantity);
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) throws ApiException {
        try{
            InventoryPojo p = dao.select(id);
            return p;
        }
        catch (Exception exception){
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
    }
}
