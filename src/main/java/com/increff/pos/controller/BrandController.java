package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BrandController {
    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a brand and category")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public String add(@RequestBody BrandForm form) throws Exception {
        return dto.add(form);
    }

    @ApiOperation(value = "Adds multiple brand and category pair")
    @RequestMapping(path = "/api/brand/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<BrandForm> form) throws Exception {
        dto.bulkAdd(form);
    }

    @ApiOperation(value = "Deletes")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an brand by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return dto.getById(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm f) throws ApiException {
        dto.update(id, f);
    }
}
