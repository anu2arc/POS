package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.Data.BrandData;
import com.increff.pos.model.Form.BrandForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/brand")
public class BrandController {
    @Autowired
    private BrandDto brandDto;
    @ApiOperation(value = "Adds a brand and category")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String add(@RequestBody BrandForm form) throws Exception {
        return brandDto.add(form);
    }
    @ApiOperation(value = "Adds multiple brand and category pair")
    @RequestMapping(path = "/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<BrandForm> form) throws Exception {
        brandDto.bulkAdd(form);
    }
    @ApiOperation(value = "Gets an brand by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return brandDto.getById(id);
    }
    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }
    @ApiOperation(value = "Updates an brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException {
        brandDto.update(id, brandForm);
    }
}
