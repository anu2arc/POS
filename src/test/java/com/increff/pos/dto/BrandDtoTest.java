package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.Data.BrandData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    private BrandForm createBrand() {
        return createBrand("nike","shoes");
    }
    private BrandForm createBrand(String brand, String category){
        BrandForm brandForm=new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    @Test
    public void testAdd() throws Exception {
        brandDto.add(createBrand());
    }
//todo use annotations
    @Test
    public void testAdd2() throws Exception {
        brandDto.add(createBrand());
        try{
            brandDto.add(createBrand());
        }
        catch (ApiException exception){
            assertEquals("Brand and Category pair already exist",exception.getMessage().trim());
        }
    }

    @Test
    public void testBulkAdd() throws ApiException {
        List<BrandForm> brandFormList=new ArrayList<>();
        brandFormList.add(createBrand());
        brandFormList.add(createBrand("levis","jeans"));
        brandDto.bulkAdd(brandFormList);

        List<BrandData> list=brandDto.getAll();

        assertEquals(brandFormList.size(),list.size());

        assertEquals(brandFormList.get(0).getBrand(),list.get(0).getBrand());
        assertEquals(brandFormList.get(0).getCategory(),list.get(0).getCategory());

        assertEquals(brandFormList.get(1).getBrand(),list.get(1).getBrand());
        assertEquals(brandFormList.get(1).getCategory(),list.get(1).getCategory());
    }

    @Test
    public void testBulkAddWithWrongData() throws ApiException {
        List<BrandForm> brandFormList=new ArrayList<>();
        brandFormList.add(createBrand());
        brandFormList.add(createBrand("","jeans"));
        brandFormList.add(createBrand("nike",""));
        try {
            brandDto.bulkAdd(brandFormList);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage().trim());
            assertEquals("2: Brand cannot be empty\n3: Category cannot be empty",exception.getMessage().trim());
        }

    }

    @Test
    public void testGetById() throws Exception {
        BrandForm brandForm=createBrand();
        brandDto.add(brandForm);
        BrandData form=brandDto.getAll().get(0);
        assertEquals(brandForm.getBrand(),form.getBrand());
        assertEquals(brandForm.getCategory(),form.getCategory());
    }

    @Test
    public void testUpdate() throws Exception {
        BrandForm brandForm=createBrand();
        brandDto.add(brandForm);
        brandForm.setBrand("adidas");
        BrandData data=brandDto.getAll().get(0);
        brandDto.update(data.getId(),brandForm);
        BrandForm form=brandDto.getById(data.getId());
        assertEquals(brandForm.getBrand(),form.getBrand());
    }
}
