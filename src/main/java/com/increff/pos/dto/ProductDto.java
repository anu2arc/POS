package com.increff.pos.dto;

import com.increff.pos.Util.ProductUtil;
import com.increff.pos.model.Data.ProductData;
import com.increff.pos.model.Form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    private void check(ProductForm form) throws ApiException {
        ProductPojo productPojo=null;
        try{
            productPojo=service.getByBarcode(form.getBarcode());
        }
        catch (Exception ignored){
        }
        if(productPojo!=null)
            throw new ApiException("Barcode already exist");
    }
    public String add(ProductForm productForm) throws Exception {
        productUtil.validate(productForm);
        check(productForm);
        ProductPojo pojo= DtoHelper.convert(productForm);
        BrandPojo brandPojo=brandService.checkPair(productForm.getBrand(),productForm.getCategory());
        pojo.setBrandCategory(brandPojo.getId());
        service.add(pojo);
        return "Product added successfully";
    }

    public void bulkAdd(List<ProductForm> productFormList) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        List<ProductPojo> list=new ArrayList<>();
        for(int i = 0; i<productFormList.size(); i++){
            ProductPojo productPojo=new ProductPojo();
            try {
                productUtil.validate(productFormList.get(i));
                check(productFormList.get(i));
                productPojo= DtoHelper.convert(productFormList.get(i));
            } catch (ApiException e) {
                errorLog.append(i + 1).append(": ").append(e.getMessage()).append("\n");
            }// todo
            try{
                BrandPojo brandPojo=brandService.checkPair(productFormList.get(i).getBrand(),productFormList.get(i).getCategory());
                productPojo.setBrandCategory(brandPojo.getId());
                list.add(productPojo);
            }
            catch (Exception e){
                errorLog.append(i + 1).append(": please provide a valid brand category pair"); // todo change error message
            }
        }
        if(errorLog.length()>0)
            throw new ApiException(errorLog.toString());
        // todo make a set and check for barcode here only
        service.bulkAdd(list);
    }

    public void update(String barcode, ProductForm productForm) throws ApiException {
        productUtil.validate(productForm);
        ProductPojo productPojo=DtoHelper.convert(productForm);
        try{
            BrandPojo brandPojo=brandService.checkPair(productForm.getBrand(),productForm.getCategory());
            productPojo.setBrandCategory(brandPojo.getId());
        }
        catch (Exception exception){
            throw new ApiException("please provide a valid brand category pair");
        }
        service.update(barcode,productPojo);
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
        if (barcode.isEmpty())
            throw new ApiException("Barcode cannot be empty");
        return DtoHelper.convert(service.getByBarcode(barcode));
    }
}
