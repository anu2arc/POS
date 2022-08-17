package com.increff.pos.Util;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;


@Repository
public class BrandUtil {
    public static void validate(BrandForm form) throws ApiException {
        normalize(form);
        if(Objects.equals(form.getBrand(), ""))
            throw new ApiException("Brand cannot be empty");
        if(form.getBrand().length()>20)
            throw new ApiException("Brand name too long");
        if(Objects.equals(form.getCategory(), ""))
            throw new ApiException("Category cannot be empty");
        if(form.getCategory().length()>20)
            throw new ApiException("Category name too long");
    }
    protected static void normalize(BrandForm brandPojo) {
        brandPojo.setBrand(brandPojo.getBrand().toLowerCase().trim());
        brandPojo.setCategory(brandPojo.getCategory().toLowerCase().trim());
    }
}