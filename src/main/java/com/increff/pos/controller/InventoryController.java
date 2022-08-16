package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryController {
    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Adds an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.POST)
    public void add(@PathVariable Integer id,@RequestBody InventoryForm form) throws Exception {
        dto.add(id,form);
    }

    @ApiOperation(value = "Adds multiple inventory")
    @RequestMapping(path = "/api/inventory/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<InventoryForm> forms) throws Exception {
        dto.bulkAdd(forms);
    }

    @ApiOperation(value = "Deletes inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an inventory by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        return dto.getById(id);
    }

    @ApiOperation(value = "Gets an inventory by Barcode")
    @RequestMapping(path = "/api/inventory/barcode/{barcode}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable String barcode) throws ApiException {
        return dto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets a list of all inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm form) throws ApiException {
        dto.update(id, form);
    }
}
