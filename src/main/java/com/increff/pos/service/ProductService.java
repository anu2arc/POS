package com.increff.pos.service;

import com.increff.pos.Util.ProductUtil;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductUtil productUtil;


    @Transactional(rollbackOn = ApiException.class)
    public Integer add(ProductPojo productPojo) {
        return productDao.insert(productPojo).getId();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<ProductPojo> productPojoList) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        for(int i = 0; i<productPojoList.size(); i++) {
            try {
                productDao.selectBarcode(productPojoList.get(i).getBarcode());
                errorLog.append(i + 1).append(": Duplicate barcode").append("\n");
            }
            catch (Exception exception) {
                productDao.insert(productPojoList.get(i));
            }
        }
        if(!errorLog.toString().isEmpty())
            throw new ApiException(errorLog.toString());
    }
    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(Integer id) throws ApiException {
        try {
            return productDao.select(id);
        }
        catch (Exception exception) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getByBarcode(String barcode) throws ApiException {
        try {
            return productDao.selectBarcode(barcode);
        }
        catch (Exception exception) {
            throw new ApiException("Product with given barcode does not exit, barcode: " + barcode);
        }
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(String barcode, ProductPojo pojo) throws ApiException {
        ProductPojo productPojo = getByBarcode(barcode);
        productPojo.setBarcode(pojo.getBarcode());
        productPojo.setMrp(pojo.getMrp());
        productPojo.setName(pojo.getName());
    }
}
