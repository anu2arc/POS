package com.increff.pos.service;

import com.increff.pos.Util.BrandUtil;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao dao;
    @Autowired
    private BrandUtil brandUtil;

    @Transactional(rollbackOn = ApiException.class)
    public Integer add(BrandPojo p) {
        normalize(p);
        return dao.insert(p).getId();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<BrandPojo> p) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        for(Integer i=0;i<p.size();i++){
            normalize(p.get(i));
            try {
                dao.setIsPresent(p.get(i).getBrand(),p.get(i).getCategory());
                errorLog.append((i+1) + ": Brand Category pair already exist"+"\n");
            } catch (Exception e) {
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
    public BrandPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(BrandPojo p) throws ApiException {
        normalize(p);
        BrandPojo ex = getCheck(p.getId());
        ex.setBrand(p.getBrand());
        ex.setCategory(p.getCategory());
    }

    public BrandPojo checkPair(String brand,String Category){
        return dao.setIsPresent(brand, Category);
    }
    @Transactional
    public BrandPojo getCheck(Integer id) throws ApiException {
        try{
            BrandPojo p = dao.select(id);
            return p;
        }
        catch (Exception exception){
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
    }

    protected static void normalize(BrandPojo p) {
        p.setBrand(p.getBrand().toLowerCase().trim());
        p.setCategory(p.getCategory().toLowerCase().trim());
    }
}
