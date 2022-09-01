package com.increff.pos.dto;

import com.increff.pos.Util.BrandUtil;
import com.increff.pos.model.Data.BrandData;
import com.increff.pos.model.Form.BrandForm;
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
    private BrandService brandService;
    public String add(BrandForm form) throws ApiException {
        BrandUtil.validate(form);
        check(form);
        BrandPojo brandPojo= DtoHelper.convert(form);
        brandService.add(brandPojo);
        return "Brand and Category added successfully";
    }
    public void bulkAdd(List<BrandForm> forms) throws ApiException {
        List<BrandPojo> list=new ArrayList<>();
        StringBuilder errorLog=new StringBuilder();
        int row=0;
        for(BrandForm form:forms){
            row++;
            try {
                BrandUtil.validate(form);
                check(form);
                list.add(DtoHelper.convert(form));
                System.out.println("clear");
            } catch (ApiException e) {
                System.out.println("exception");
                errorLog.append(row).append(": ").append(e.getMessage()).append("\n");
            }
        }
        if(!errorLog.toString().isEmpty())
            throw new ApiException(errorLog.toString());
        brandService.bulkAdd(list);
    }

    public void update(Integer id, BrandForm form) throws ApiException {
        BrandUtil.validate(form);
        check(form);
        BrandPojo brandPojo= DtoHelper.convert(form);
        brandPojo.setId(id);
        brandService.update(brandPojo);
    }
    public List<BrandData> getAll() {
        List<BrandPojo> brandPojos= brandService.getAll();
        List<BrandData> brandDataList=new ArrayList<>();
        for(BrandPojo brandPojo:brandPojos){
            brandDataList.add(DtoHelper.convert(brandPojo));
        }
        return brandDataList;
    }

    public BrandData getById(Integer id) throws ApiException {
        return DtoHelper.convert(brandService.get(id));
    }
    private void check(BrandForm form) throws ApiException {
        BrandPojo brandPojo=null;
        try{
            brandPojo=brandService.checkPair(form.getBrand(),form.getCategory());
        }
        catch (Exception ignored){
        }
        if(brandPojo!=null)
            throw new ApiException("Brand and Category pair already exist");
    }
}
