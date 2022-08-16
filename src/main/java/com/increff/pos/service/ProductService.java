package com.increff.pos.service;

import com.increff.pos.Util.ProductUtil;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao dao;

    @Autowired
    private ProductUtil productUtil;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Transactional(rollbackOn = ApiException.class)
    public Integer add(ProductPojo p) {
        normalize(p);
        return dao.insert(p).getId();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<ProductPojo> p) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        for(Integer i=0;i<p.size();i++) {
            normalize(p.get(i));
            try {
                dao.selectBarcode(p.get(i).getBarcode());
                errorLog.append((i+1) + ": Duplicate barcode"+"\n");
            }
            catch (Exception e) {
                dao.insert(p.get(i));
            }
        }
        if(!errorLog.toString().isEmpty())
        throw new ApiException(errorLog.toString());
    }

    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getByBarcode(String barcode) throws ApiException {
        return getCheckBarcode(barcode);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(String barcode, ProductPojo p) throws ApiException {
        normalize(p);
        ProductPojo ex = getCheckBarcode(barcode);
        ex.setBarcode(p.getBarcode());
        ex.setMrp(p.getMrp());
        ex.setName(p.getName());
    }

    @Transactional
    public ProductPojo getCheckBarcode(String barcode) throws ApiException {
        try {
            ProductPojo p = dao.selectBarcode(barcode);
            return p;
        }
        catch (Exception e) {
            throw new ApiException("Product with given barcode does not exit, barcode: " + barcode);
        }
    }
    @Transactional
    public ProductPojo getCheck(Integer id) throws ApiException {
        try {
            ProductPojo p = dao.select(id);
            return p;
        }
        catch (Exception e) {
            throw new ApiException("Product with given barcode does not exit, id: " + id);
        }
    }
    protected static void normalize(ProductPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
        p.setName((p.getName().toLowerCase().trim()));
        p.setMrp(Double.valueOf(df.format(p.getMrp())));
    }
}
