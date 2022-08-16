package com.increff.pos.dto;

import com.increff.pos.Util.BrandUtil;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BrandDto {

    @Autowired
    private BrandService service;
    @Autowired
    private BrandUtil brandUtil;

    public String add(BrandForm form) throws Exception {
        brandUtil.validate(form);
        BrandPojo brandPojo= DtoHelper.convert(form);
        service.add(brandPojo);
        return "Brand and Category added successfully";
    }

    public void bulkAdd(List<BrandForm> forms) throws ApiException {
        List<BrandPojo> list=new ArrayList<>();
        StringBuilder errorLog=new StringBuilder();
        for(Integer i=0;i<forms.size();i++){
            try {
                brandUtil.validate(forms.get(i));
                list.add(DtoHelper.convert(forms.get(i)));
            } catch (ApiException e) {
                errorLog.append((i+1) + ": "+e.getMessage()+"\n");
            }
        }
        if(!errorLog.toString().isEmpty())
            throw new ApiException(errorLog.toString());
        service.bulkAdd(list);
    }

    public void update(Integer id, BrandForm form) throws ApiException {
        brandUtil.validate(form);
        BrandPojo brandPojo= DtoHelper.convert(form);
        brandPojo.setId(id);
        service.update(brandPojo);
    }
    public List<BrandData> getAll() {
        List<BrandPojo> brandPojos=service.getAll();
        List<BrandData> brandDataList=new ArrayList<>();
        for(BrandPojo brandPojo:brandPojos){
            brandDataList.add(DtoHelper.convert(brandPojo));
        }
        return brandDataList;
    }

    public BrandData getById(Integer id) throws ApiException {
        return DtoHelper.convert(service.get(id));
    }

    public void delete(Integer id) {
        service.delete(id);
    }
}
