package com.increff.pos.dto;

import com.increff.pos.Util.ProductUtil;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductFrom;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductUtil productUtil;
    @Autowired
    private BrandService brandService;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public String add(ProductFrom f) throws Exception {
        productUtil.validate(f);
        normalize(f);
        ProductPojo p= DtoHelper.convert(f);
        try{
            BrandPojo brandPojo=brandService.checkPair(f.getBrand(),f.getCategory());
            p.setBrand_category(brandPojo.getId());
        }
        catch (Exception e){
            throw new ApiException("please provide a valid brand category pair");
        }
        service.add(p);
        return "Product added successfully";
    }

    public void bulkAdd(List<ProductFrom> p) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        List<ProductPojo> list=new ArrayList<>();
        for(Integer i=0;i<p.size();i++){
            normalize(p.get(i));
            ProductPojo temp=new ProductPojo();
            try {
                productUtil.validate(p.get(i));
                temp= DtoHelper.convert(p.get(i));
            } catch (ApiException e) {
                errorLog.append((i+1) + ": "+e.getMessage()+"\n");
            }
            try{
                BrandPojo brandPojo=brandService.checkPair(p.get(i).getBrand(),p.get(i).getCategory());
                temp.setBrand_category(brandPojo.getId());
                list.add(temp);
            }
            catch (Exception e){
                errorLog.append((i+1) + ": please provide a valid brand category pair");
            }
        }
        if(errorLog.length()>0)
            throw new ApiException(errorLog.toString());
        service.bulkAdd(list);
    }

    public void update(String barcode, ProductFrom p) throws ApiException {
        productUtil.validate(p);
        normalize(p);
        ProductPojo f= DtoHelper.convert(p);
        try{
            BrandPojo brandPojo=brandService.checkPair(p.getBrand(),p.getCategory());
            f.setBrand_category(brandPojo.getId());
        }
        catch (Exception e){
            throw new ApiException("please provide a valid brand category pair");
        }
        service.update(barcode,f);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list=service.getAll();;
        List<ProductData> productDataList=new ArrayList<>();
        for(ProductPojo item:list){
            productDataList.add(DtoHelper.convert(item));
        }
        return  productDataList;
    }

    public ProductData getById(Integer id) throws ApiException {
        return DtoHelper.convert(service.get(id));
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        if(barcode.isEmpty())
            throw new ApiException("Barcode cannot be empty");
        return DtoHelper.convert(service.getByBarcode(barcode));
    }

    public void delete(Integer id) {
        service.delete(id);
    }
    protected static void normalize(ProductFrom p) {
        if(!p.getBarcode().isEmpty())
        p.setBarcode(p.getBarcode().toLowerCase().trim());
        p.setName((p.getName().toLowerCase().trim()));
        p.setMrp(Double.valueOf(df.format(p.getMrp())));
    }
}
