package com.increff.pos.Util;

import com.increff.pos.model.Form.ProductFrom;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.Objects;

@Repository
public class ProductUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public void validate(ProductFrom from) throws ApiException {
        if(Objects.isNull(from.getBarcode()) || Objects.equals(from.getBarcode(), ""))
            throw new ApiException("Barcode cannot be empty");
        if(from.getBarcode().length()>20)
            throw new ApiException("Barcode length is too long");
        if(from.getBrand().equals(""))
            throw new ApiException("please provide a brand");
        if(from.getCategory().equals(""))
            throw new ApiException("please provide a category");
        if(Objects.isNull(from.getName()) || Objects.equals(from.getName(), ""))
            throw new ApiException("Product Name cannot be empty");
        if(from.getName().length()>20)
            throw new ApiException("Name length is too long");
        if(from.getMrp()==null)
            throw new ApiException("MRP cannot be empty");
        if(from.getMrp().isNaN())
            throw new ApiException("Please provide a valid mrp");
        if(from.getMrp()==0)
            throw new ApiException("MRP cannot be zero");
        if(from.getMrp()<0)
            throw new ApiException("MRP cannot be negative value");
        if(from.getMrp()>1000000)
            throw new ApiException("MRP value exceeded the max limit");
        normalize(from);
    }
    protected static void normalize(ProductFrom from) {
        from.setBarcode(from.getBarcode().toLowerCase().trim());
        from.setName((from.getName().toLowerCase().trim()));
        from.setMrp(Double.valueOf(DECIMAL_FORMAT.format(from.getMrp())));
    }
}
