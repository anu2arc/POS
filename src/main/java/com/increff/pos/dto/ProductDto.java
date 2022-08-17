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
    private void check(ProductFrom form) throws ApiException {
        ProductPojo productPojo=null;
        try{
            productPojo=service.getCheckBarcode(form.getBarcode());
        }
        catch (Exception ignored){
        }
        if(productPojo!=null)
            throw new ApiException("Barcode already exist");
    }
    public String add(ProductFrom productFrom) throws Exception {
        productUtil.validate(productFrom);
        check(productFrom);
        ProductPojo p= DtoHelper.convert(productFrom);
        try{
            BrandPojo brandPojo=brandService.checkPair(productFrom.getBrand(),productFrom.getCategory());
            p.setBrand_category(brandPojo.getId());
        }
        catch (Exception e){
            throw new ApiException("please provide a valid brand category pair");
        }
        service.add(p);
        return "Product added successfully";
    }

    public void bulkAdd(List<ProductFrom> productFromList) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        List<ProductPojo> list=new ArrayList<>();
        for(int i = 0; i<productFromList.size(); i++){
            ProductPojo productPojo=new ProductPojo();
            try {
                productUtil.validate(productFromList.get(i));
                check(productFromList.get(i));
                productPojo= DtoHelper.convert(productFromList.get(i));
            } catch (ApiException e) {
                errorLog.append(i + 1).append(": ").append(e.getMessage()).append("\n");
            }
            try{
                BrandPojo brandPojo=brandService.checkPair(productFromList.get(i).getBrand(),productFromList.get(i).getCategory());
                productPojo.setBrand_category(brandPojo.getId());
                list.add(productPojo);
            }
            catch (Exception e){
                errorLog.append(i + 1).append(": please provide a valid brand category pair");
            }
        }
        if(errorLog.length()>0)
            throw new ApiException(errorLog.toString());
        service.bulkAdd(list);
    }

    public void update(String barcode, ProductFrom productFrom) throws ApiException {
        productUtil.validate(productFrom);
        check(productFrom);
        ProductPojo f= DtoHelper.convert(productFrom);
        try{
            BrandPojo brandPojo=brandService.checkPair(productFrom.getBrand(),productFrom.getCategory());
            f.setBrand_category(brandPojo.getId());
        }
        catch (Exception exception){
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
}
