package com.increff.pos.controller;


import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.Data.ProductData;
import com.increff.pos.model.Form.ProductForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String add(@RequestBody ProductForm form) throws Exception {
        return dto.add(form);
    }

    @ApiOperation(value = "Adds multiple product")
    @RequestMapping(path = "/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<ProductForm> form) throws Exception {
        dto.bulkAdd(form);
    }

    @ApiOperation(value = "Gets an product by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        return dto.getById(id);
    }

    @ApiOperation(value = "Gets an product by barcode")
    @RequestMapping(path = "/barcode/{barcode}", method = RequestMethod.GET)
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
        return dto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an product")
    @RequestMapping(path = "/{barcode}", method = RequestMethod.PUT)
    public void update(@PathVariable String barcode, @RequestBody ProductForm productForm) throws ApiException {
        dto.update(barcode, productForm);
    }
}
