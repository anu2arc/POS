package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.Data.InventoryData;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Adds an inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public void add(@PathVariable Integer id,@RequestBody InventoryForm form) throws Exception {
        inventoryDto.add(id,form);
    }

    @ApiOperation(value = "Adds multiple inventory")
    @RequestMapping(path = "/bulk-add", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<InventoryForm> forms) throws Exception {
        inventoryDto.bulkAdd(forms);
    }

    @ApiOperation(value = "Gets an inventory by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        return inventoryDto.getById(id);
    }

    @ApiOperation(value = "Gets an inventory by Barcode")
    @RequestMapping(path = "/barcode/{barcode}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable String barcode) throws ApiException {
        return inventoryDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets a list of all inventory")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm form) throws ApiException {
        inventoryDto.update(id, form);
    }
}
