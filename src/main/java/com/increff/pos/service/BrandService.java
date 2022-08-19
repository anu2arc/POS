package com.increff.pos.service;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;
    @Transactional(rollbackOn = ApiException.class)
    public Integer add(BrandPojo brandPojo) {
        return brandDao.insert(brandPojo).getId();
    }
    @Transactional(rollbackOn = ApiException.class)
    public void bulkAdd(List<BrandPojo> brandPojoList) throws ApiException {
        StringBuilder errorLog=new StringBuilder();
        for(int i = 0; i<brandPojoList.size(); i++){
            try {
                brandDao.setIsPresent(brandPojoList.get(i).getBrand(),brandPojoList.get(i).getCategory());
                errorLog.append(i + 1).append(": Brand Category pair already exist").append("\n");
            } catch (Exception exception) {
                brandDao.insert(brandPojoList.get(i));
            }
        }
        if(!errorLog.toString().isEmpty())
            throw new ApiException(errorLog.toString());
    }
    // TODO :: remove delete function
    @Transactional
    public void delete(Integer id) {
        brandDao.delete(id);
    }
    @Transactional
    public BrandPojo get(Integer id) throws ApiException {
        try{
            return brandDao.select(id);
        }
        catch (Exception exception){
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
    }
    @Transactional
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }
    @Transactional(rollbackOn  = ApiException.class)
    public void update(BrandPojo brandPojo) throws ApiException {
        BrandPojo pojo = get(brandPojo.getId());
        pojo.setBrand(brandPojo.getBrand());
        pojo.setCategory(brandPojo.getCategory());
    }
    public BrandPojo checkPair(String brand,String Category){
        return brandDao.setIsPresent(brand, Category);
    }
}
