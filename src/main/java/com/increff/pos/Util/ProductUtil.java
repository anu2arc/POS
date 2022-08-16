package com.increff.pos.Util;

import com.increff.pos.model.ProductFrom;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ProductUtil {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    public void validate(ProductFrom p) throws ApiException {
        if(Objects.isNull(p.getBarcode()) || p.getBarcode()=="")
            throw new ApiException("Barcode cannot be empty");
        if(p.getBarcode().length()>20)
            throw new ApiException("Barcode length is too long");
        if(p.getBrand().equals(""))
            throw new ApiException("please provide a brand");
        if(p.getCategory().equals(""))
            throw new ApiException("please provide a category");
        if(Objects.isNull(p.getName()) || p.getName()=="")
            throw new ApiException("Product Name cannot be empty");
        if(p.getName().length()>20)
            throw new ApiException("Name length is too long");
        if(p.getMrp()==null)
            throw new ApiException("MRP cannot be empty");
        if(p.getMrp().isNaN())
            throw new ApiException("Please provide a valid mrp");
        if(p.getMrp()==0)
            throw new ApiException("MRP cannot be zero");
        if(p.getMrp()<0)
            throw new ApiException("MRP cannot be negative value");
        if(p.getMrp()>1000000)
            throw new ApiException("MRP value exceeded the max limit");
        List<ProductPojo> products= productService.getAll();
        for(ProductPojo product:products){
            if(product.getBarcode().equals(p.getBarcode()))
                throw new ApiException("Barcode already exist");
        }
    }
}
