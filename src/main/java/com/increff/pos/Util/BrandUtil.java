package com.increff.pos.Util;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class BrandUtil {
    @Autowired
    private BrandService service;

    @Autowired
    private BrandDao dao;
    public void validate(BrandForm p) throws ApiException {
        if( p.getBrand()=="")
            throw new ApiException("Brand cannot be empty");
        if(p.getBrand().length()>20)
            throw new ApiException("Brand name too long");
        if(p.getCategory()=="")
            throw new ApiException("Category cannot be empty");
        if(p.getCategory().length()>20)
            throw new ApiException("Category name too long");
        BrandPojo brandPojo=null;
        try{
            brandPojo=dao.setIsPresent(p.getBrand(),p.getCategory());
        }
        catch (Exception e){
        }
        finally {
            if(brandPojo!=null)
                throw new ApiException("Brand and Category pair already exist");
        }
    }
}
