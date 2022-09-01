package com.increff.pos.dto;
import com.increff.pos.Util.OrderItemUtil;
import com.increff.pos.Util.OrderUtil;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.OrderForm;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderUtil orderUtil;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderItemUtil orderItemUtil;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemDto orderItemDto;
    public OrderData add(){
        return DtoHelper.convert(orderService.add());
    }

    public List<OrderData> getAll() {
        List<OrderPojo> list= orderService.getAll();
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo orderPojo:list){
            orderDataList.add(DtoHelper.convert(orderPojo));
        }
        return orderDataList;
    }

    public OrderData getById(Integer id) throws ApiException {
        return DtoHelper.convert(orderService.get(id));
    }

    public List<OrderData> getByRange(OrderForm form) throws ApiException {
        ZonedDateTime start=orderUtil.convert(form.getStartDate());
        ZonedDateTime end=orderUtil.convert(form.getEndDate());
        orderUtil.validate(start,end);
        List<OrderPojo> list= orderService.getByRange(start,end);
        List<OrderData> orderDataList=new ArrayList<>();
        for(OrderPojo item:list){
            orderDataList.add(DtoHelper.convert(item));
        }
        return orderDataList;
    }

    public void getInvoice(HttpServletResponse response, int orderId) throws IOException, TransformerException, ApiException {
        invoiceService.getOrderInvoice(response,orderId);
    }

    public String addOrderItem(List<OrderItemForm> orderItemForms) throws ApiException {
        StringBuilder response=new StringBuilder();
        Set<String> barcodeHolder=new HashSet<>();
        int rowNo=0;
        for(OrderItemForm orderItem:orderItemForms) {
            rowNo++;
            try{
                orderItemUtil.validate(orderItem);
                check(orderItem);
                if(barcodeHolder.contains(orderItem.getBarcode()))
                    throw new ApiException((rowNo)+": Repeated Order Item");
                barcodeHolder.add(orderItem.getBarcode());
            }
            catch (ApiException exception){
                response.append(exception.getMessage()).append("\n");
            }
        }
        if(!response.toString().isEmpty())
            throw new ApiException(response.toString());
        OrderPojo orderPojo=orderService.add();
        return orderItemDto.add(orderItemForms,orderPojo.getId());
    }
    private void check(OrderItemForm orderItem) throws ApiException {
        ProductPojo productPojo=productService.getByBarcode(orderItem.getBarcode());
        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());
        if(inventoryPojo.getQuantity()< orderItem.getQuantity())
            throw new ApiException("Max Quantity for product "+orderItem.getBarcode()+" is :"+inventoryPojo.getQuantity());
        if(orderItem.getSellingprice()> productPojo.getMrp())
            throw new ApiException("Selling price cannot be more than MRP for Product :"+orderItem.getBarcode());
    }
}
