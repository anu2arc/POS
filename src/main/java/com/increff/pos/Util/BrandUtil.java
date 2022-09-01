package com.increff.pos.Util;

import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import java.util.Objects;


@Repository
public class BrandUtil {
    public static void validate(BrandForm form) throws ApiException {
        System.out.println(form.getBrand());
        System.out.println(form.getCategory());
        if(Objects.isNull(form.getBrand()) || form.getBrand().trim().equals(""))
            throw new ApiException("Brand cannot be empty");
        if(form.getBrand().length()>20)
            throw new ApiException("Brand name too long");
        if(Objects.isNull(form.getCategory()) || form.getCategory().trim().equals(""))
            throw new ApiException("Category cannot be empty");
        if(form.getCategory().length()>20)
            throw new ApiException("Category name too long");
        normalize(form);
    }
    protected static void normalize(BrandForm brandPojo) {
        brandPojo.setBrand(brandPojo.getBrand().toLowerCase().trim());
        brandPojo.setCategory(brandPojo.getCategory().toLowerCase().trim());
    }
}
