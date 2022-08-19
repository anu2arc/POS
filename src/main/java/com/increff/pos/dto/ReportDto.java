package com.increff.pos.dto;

import com.increff.pos.model.Data.InventoryReportData;
import com.increff.pos.model.Form.ReportForm;
import com.increff.pos.model.Data.SalesReportData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportDto {
    @Autowired
    private ReportService service;
    public List<SalesReportData> salesReport(ReportForm form) throws ApiException {
        return service.generate(form);
    }
    public List<InventoryReportData> inventoryReport() throws ApiException {
        return service.iReport();
    }
}
