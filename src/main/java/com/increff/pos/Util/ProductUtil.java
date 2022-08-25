package com.increff.pos.Util;

import com.increff.pos.model.Form.ProductForm;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.Objects;

@Repository
public class ProductUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public void validate(ProductForm form) throws ApiException {
        if(Objects.isNull(form.getBarcode()) || form.getBarcode().equals(""))
            throw new ApiException("Barcode cannot be empty");
        if(form.getBarcode().length()>20)
            throw new ApiException("Barcode length is too long");
        if(Objects.isNull(form.getBrand()) ||form.getBrand().equals(""))
            throw new ApiException("Brand cannot be empty");
        if(Objects.isNull(form.getCategory()) ||form.getCategory().equals(""))
            throw new ApiException("Category cannot be empty");
        if(Objects.isNull(form.getName()) || Objects.equals(form.getName(), ""))
            throw new ApiException("Product Name cannot be empty");
        if(form.getName().length()>20)
            throw new ApiException("Name length is too long");
        if(form.getMrp()==null)
            throw new ApiException("MRP cannot be empty");
        if(form.getMrp().isNaN())
            throw new ApiException("Invalid MRP");
        if(form.getMrp()==0)
            throw new ApiException("MRP cannot be zero");
        if(form.getMrp()<0)
            throw new ApiException("MRP cannot be negative value");
        if(form.getMrp()>1000000)
            throw new ApiException("MRP value exceeded the max limit");
        normalize(form);
    }
    protected static void normalize(ProductForm productFrom) {
        productFrom.setBarcode(productFrom.getBarcode().toLowerCase().trim());
        productFrom.setName((productFrom.getName().toLowerCase().trim()));
        productFrom.setMrp(Double.valueOf(DECIMAL_FORMAT.format(productFrom.getMrp())));
    }
}
