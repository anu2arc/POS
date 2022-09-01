package com.increff.pos.service;

import com.increff.pos.model.Data.InventoryReportData;
import com.increff.pos.model.Data.SalesReportData;
import com.increff.pos.model.Form.ReportForm;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;
    @Transactional(rollbackOn = ApiException.class)
    public List<SalesReportData> salesReport(ReportForm form) throws ApiException {
        List<OrderPojo> orderInDateRange=orderService.getByRange(form.getStartDate(),form.getEndDate());
        List<OrderItemPojo> orderItemList=new ArrayList<>();
        for(OrderPojo order:orderInDateRange){
            orderItemList.addAll(orderItemService.getOrder(order.getId()));
        }
        HashMap<String,HashMap<String, SalesReportData>> holder = new HashMap<String, HashMap<String, SalesReportData>>();
        List<SalesReportData> list=new ArrayList<>();
        for(OrderItemPojo orderItem: orderItemList){
            ProductPojo productPojo=productService.get(orderItem.getProductId());
            BrandPojo brandPojo=brandService.get(productPojo.getBrandCategory());
            if((form.getBrand().equals("") || form.getBrand().equals(brandPojo.getBrand()))
                    && (form.getCategory().equals("")) || form.getCategory().equals(brandPojo.getCategory())){
                SalesReportData reportData=new SalesReportData();
                reportData.setBrand(brandPojo.getBrand());
                reportData.setCategory(brandPojo.getCategory());
                reportData.setQuantity(orderItem.getQuantity());
                reportData.setRevenue(orderItem.getQuantity()*orderItem.getSellingPrice());
                if(holder.containsKey(reportData.getBrand()) && holder.get(reportData.getBrand()).containsKey(reportData.getCategory())){
                    SalesReportData rd=holder.get(reportData.getBrand()).get(reportData.getCategory());
                    holder.get(reportData.getBrand()).get(reportData.getCategory()).setQuantity(rd.getQuantity()+ reportData.getQuantity());
                    holder.get(reportData.getBrand()).get(reportData.getCategory()).setRevenue(rd.getRevenue()+ reportData.getRevenue());
                }
                else {
                    HashMap<String, SalesReportData> temp=new HashMap<>();
                    temp.put(reportData.getCategory(),reportData);
                    holder.put(reportData.getBrand(),temp);
                }
            }
        }
        for(HashMap<String, SalesReportData> reportData:holder.values()){
            list.addAll(reportData.values());
        }
        if(list.size()==0)
            throw new ApiException("No report for given input");
        return list;
    }

    public List<InventoryReportData> inventoryReport() throws ApiException {
        HashMap<String,InventoryReportData> holder = new HashMap<>();
        List<InventoryPojo> inventoryPojoList=inventoryService.getAll();
        for(InventoryPojo item:inventoryPojoList){
            ProductPojo productPojo=productService.get(item.getId());
            BrandPojo brandPojo=brandService.get(productPojo.getBrandCategory());
            if(holder.containsKey(brandPojo.getBrand()+brandPojo.getCategory())) {
                int qty=holder.get(brandPojo.getBrand()+brandPojo.getCategory()).getQuantity();
                holder.get(brandPojo.getBrand()+brandPojo.getCategory()).setQuantity(qty+ item.getQuantity());
            }
            else {
                InventoryReportData inventoryReportData = new InventoryReportData();
                inventoryReportData.setBrand(brandPojo.getBrand());
                inventoryReportData.setCategory(brandPojo.getCategory());
                inventoryReportData.setQuantity(item.getQuantity());
                holder.put(inventoryReportData.getBrand()+inventoryReportData.getCategory(),inventoryReportData);
            }
        }
        List<InventoryReportData> report = new ArrayList<>(holder.values());
        System.out.println(report.size());
        if(report.size()==0)
            throw new ApiException("No report for given input");
        return report;
    }
}