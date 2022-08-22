package com.increff.pos.dto;

import com.increff.pos.Util.InventoryUtil;
import com.increff.pos.model.Data.InventoryData;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryUtil inventoryUtil;
    @Autowired
    private ProductService productService;

    public void add(Integer id,InventoryForm form) throws Exception {
        inventoryUtil.validate(form);
        InventoryPojo inventoryPojo= DtoHelper.convert(form);
        inventoryPojo.setId(id);
        try{
            inventoryService.get(id);
            inventoryService.update(id,inventoryPojo);
        }
        catch (ApiException apiException){
            inventoryService.add(inventoryPojo);
        }
    }
    public void bulkAdd(List<InventoryForm> inventoryFormList) throws ApiException {
        List<InventoryPojo> inventoryPojoList=new ArrayList<>();
        StringBuilder errorLog=new StringBuilder();
        for(int i = 0; i<inventoryFormList.size(); i++){
            InventoryForm item=inventoryFormList.get(i);
            try{
                inventoryUtil.validate(item);
                InventoryPojo inventoryPojo= DtoHelper.convert(item);
                inventoryPojo.setId(productService.getByBarcode(item.getBarcode()).getId());
                inventoryPojoList.add(inventoryPojo);
            }
            catch (Exception exception) {
                errorLog.append(i + 1).append(": ").append(exception.getMessage()).append("\n");
            }
        }
        if(!errorLog.toString().isEmpty())
            throw new ApiException(errorLog.toString());
        inventoryService.bulkAdd(inventoryPojoList);
    }

    public void update(Integer id, InventoryForm form) throws ApiException {
        inventoryService.update(id, DtoHelper.convert(form));
    }
    
    public List<InventoryData> getAll() {
        List<InventoryPojo> list= inventoryService.getAll();
        List<InventoryData> inventoryDataList=new ArrayList<>();
        for(InventoryPojo item:list){
            inventoryDataList.add(DtoHelper.convert(item));
        }
        return inventoryDataList;
    }
    public InventoryData getById(Integer id) throws ApiException {
        return DtoHelper.convert(inventoryService.get(id));
    }
    public InventoryData getByBarcode(String barcode) throws ApiException {
        return DtoHelper.convert(inventoryService.get(productService.getByBarcode(barcode).getId()));
    }
    
}
