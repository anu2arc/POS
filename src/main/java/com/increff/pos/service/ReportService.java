package com.increff.pos.service;

import com.increff.pos.model.InventoryReport;
import com.increff.pos.model.ReportForm;
import com.increff.pos.model.SalesReport;
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
    public List<SalesReport> generate(ReportForm form) throws ApiException {

        if((form.getStartDate().toString().equals("") || form.getEndDate().toString().equals("")))
            throw new ApiException("please provide a valid input");

        List<OrderPojo> orderInDateRange=orderService.getByRange(form.getStartDate(),form.getEndDate());
        List<OrderItemPojo> orderItemList=new ArrayList<>();
        for(OrderPojo order:orderInDateRange){
            orderItemList.addAll(orderItemService.getOrder(order.getId()));
        }
        HashMap<String,HashMap<String, SalesReport>> holder = new HashMap<String, HashMap<String, SalesReport>>();
        List<SalesReport> list=new ArrayList<>();
        for(OrderItemPojo orderItem: orderItemList){
            ProductPojo productPojo=productService.get(orderItem.getProductId());
            BrandPojo brandPojo=brandService.get(productPojo.getBrand_category());
            if((form.getBrand().equals("") || form.getBrand().equals(brandPojo.getBrand()))
                    && (form.getCategory().equals("")) || form.getCategory().equals(brandPojo.getCategory())){
                SalesReport reportData=new SalesReport();
                reportData.setBrand(brandPojo.getBrand());
                reportData.setCategory(brandPojo.getCategory());
                reportData.setQuantity(orderItem.getQuantity());
                reportData.setRevenue(orderItem.getQuantity()*orderItem.getSellingPrice());
                if(holder.containsKey(reportData.getBrand()) && holder.get(reportData.getBrand()).containsKey(reportData.getCategory())){
                    SalesReport rd=holder.get(reportData.getBrand()).get(reportData.getCategory());
                    holder.get(reportData.getBrand()).get(reportData.getCategory()).setQuantity(rd.getQuantity()+ reportData.getQuantity());
                    holder.get(reportData.getBrand()).get(reportData.getCategory()).setRevenue(rd.getRevenue()+ reportData.getRevenue());
                }
                else {
                    HashMap<String, SalesReport> temp=new HashMap<>();
                    temp.put(reportData.getCategory(),reportData);
                    holder.put(reportData.getBrand(),temp);
                }
            }
        }
        for(HashMap<String, SalesReport> reportData:holder.values()){
            list.addAll(reportData.values());
        }
        return list;
    }

    public List<InventoryReport> iReport() throws ApiException {
        List<InventoryReport> report = new ArrayList<>();
        HashMap<String,HashMap<String, InventoryReport>> holder = new HashMap<String, HashMap<String, InventoryReport>>();
        List<InventoryPojo> ip=inventoryService.getAll();
        for(InventoryPojo item:ip){
            ProductPojo productPojo=productService.get(item.getId());
            BrandPojo brandPojo=brandService.get(productPojo.getBrand_category());
            if(holder.containsKey(brandPojo.getBrand()) && holder.get(brandPojo.getBrand()).containsKey(brandPojo.getCategory())) {
                Integer qty=holder.get(brandPojo.getBrand()).get(brandPojo.getCategory()).getQuantity();
                holder.get(brandPojo.getBrand()).get(brandPojo.getCategory()).setQuantity(qty+ item.getQuantity());
            }
            else {
                InventoryReport inventoryReport = new InventoryReport();
                inventoryReport.setBrand(brandPojo.getBrand());
                inventoryReport.setCategory(brandPojo.getCategory());
                inventoryReport.setQuantity(item.getQuantity());
                HashMap<String,InventoryReport> temp=new HashMap<>();
                temp.put(inventoryReport.getCategory(),inventoryReport);
                holder.put(inventoryReport.getBrand(),temp);
            }
        }
        for (HashMap<String,InventoryReport> item: holder.values()) {
            report.addAll(item.values());
        }
        return report;
    }
}