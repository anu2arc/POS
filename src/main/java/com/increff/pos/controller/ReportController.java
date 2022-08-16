package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.InventoryReport;
import com.increff.pos.model.ReportForm;
import com.increff.pos.model.SalesReport;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportController {

    @Autowired
    private ReportDto dto;

    @ApiOperation(value = "Generates sales report")
    @RequestMapping(path = "/api/reports/sales",method = RequestMethod.POST)
    public List<SalesReport> generate(@RequestBody ReportForm form) throws ApiException {
        return dto.salesReport(form);
    }

    @ApiOperation(value = "Generates inventory report")
    @RequestMapping(path = "/api/reports/inventory",method = RequestMethod.GET)
    public List<InventoryReport> inventoryReport() throws ApiException{
        return dto.inventoryReport();
    }
}
