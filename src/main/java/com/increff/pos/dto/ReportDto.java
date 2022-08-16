package com.increff.pos.dto;

import com.increff.pos.model.InventoryReport;
import com.increff.pos.model.ReportForm;
import com.increff.pos.model.SalesReport;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportDto {
    @Autowired
    private ReportService service;
    public List<SalesReport> salesReport(ReportForm form) throws ApiException {
        return service.generate(form);
    }
    public List<InventoryReport> inventoryReport() throws ApiException {
        return service.iReport();
    }
}
