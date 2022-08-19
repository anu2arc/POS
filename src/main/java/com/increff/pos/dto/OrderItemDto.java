package com.increff.pos.dto;

import com.increff.pos.Util.OrderItemUtil;
import com.increff.pos.model.Data.OrderItemData;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemUtil orderItemUtil;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;

    private void check(OrderItemForm orderItem) throws ApiException {
        ProductPojo pp=null;
        try{
            pp=productService.getByBarcode(orderItem.getBarcode());
        }
        catch (ApiException apiException){
            throw new ApiException("Invalid barcode :"+orderItem.getBarcode());
        }
        InventoryPojo ip=inventoryService.get(pp.getId());
        if(ip.getQuantity()< orderItem.getQuantity())
            throw new ApiException("Max Quantity for product "+orderItem.getBarcode()+" is :"+ip.getQuantity());
        if(orderItem.getSellingprice()> pp.getMrp())
            throw new ApiException("Selling price cannot be more than MRP for Product :"+orderItem.getBarcode());
    }
    public String add(List<OrderItemForm> orderItemForms) throws ApiException {
        StringBuilder response=new StringBuilder();
        HashMap<String,OrderItemPojo> list=new HashMap<>();
        OrderPojo orderPojo=orderService.add();
        for(int i = 0; i<orderItemForms.size(); i++) {
            OrderItemForm orderItem=orderItemForms.get(i);
            try{
                orderItemUtil.validate(orderItem);
                check(orderItem);
                if(list.containsKey(orderItem.getBarcode()))
                    throw new ApiException((i+1)+": Duplicate Product Present");
                else{
                    Integer pid=productService.getByBarcode(orderItem.getBarcode()).getId();
                    list.put(orderItem.getBarcode(), DtoHelper.convert(orderItem,orderPojo.getId(),pid));
                }
            }
            catch (ApiException apiException){
                response.append(apiException.getMessage()).append("\n");
            }
        }
        if(response.toString().isEmpty()) {
            List<OrderItemPojo> orderItemPojoList = new ArrayList<>(list.values());
            orderItemService.add(orderItemPojoList);
            response.append("Order placed Successfully with orderId :").append(orderPojo.getId());
            return response.toString();
        }
        else {
            orderService.delete(orderPojo.getId());
            throw new ApiException(response.toString());
        }
    }
    public List<OrderItemData> getOrder(Integer orderId) {
        List<OrderItemPojo> list=orderItemService.getOrder(orderId);
        List<OrderItemData> orderItemData=new ArrayList<>();
        for(OrderItemPojo item:list){
            orderItemData.add(DtoHelper.convert(item));
        }
        return orderItemData;
    }
}
