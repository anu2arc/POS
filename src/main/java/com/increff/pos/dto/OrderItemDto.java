package com.increff.pos.dto;

import com.increff.pos.Util.OrderItemUtil;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
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

    public List<String> add(List<OrderItemForm> orderItemForms) throws ApiException {
        List<String> response=new ArrayList<>();
        HashMap<String,OrderItemPojo> list=new HashMap<>();
        OrderPojo op=orderService.add();
        for(Integer i=0;i<orderItemForms.size();i++) {
            OrderItemForm orderItem=orderItemForms.get(i);
            try{
                orderItemUtil.validate(orderItem);
                if(list.containsKey(orderItem.getBarcode()))
                    throw new ApiException((i+1)+": Duplicate Product Present");
                else{
                    Integer pid=productService.getByBarcode(orderItem.getBarcode()).getId();
                    list.put(orderItem.getBarcode(), DtoHelper.convert(orderItem,op.getId(),pid));
                }
            }
            catch (ApiException apiException){
                response.add(apiException.getMessage());
            }
        }
        if(response.size()==0) {
            List<OrderItemPojo> orderItemPojoList= new ArrayList<>();
            for(OrderItemPojo itemPojo:list.values())
                orderItemPojoList.add(itemPojo);
            orderItemService.add(orderItemPojoList);
            response.add("Order placed Successfully with orderId :"+op.getId());
        }
        else {
            orderService.delete(op.getId());
        }
        return response;
    }

    public List<OrderItemData> getOrder(Integer orderId) {
        List<OrderItemPojo> list=orderItemService.getOrder(orderId);
        List<OrderItemData> orderitem=new ArrayList<>();
        for(OrderItemPojo item:list){
            orderitem.add(DtoHelper.convert(item));
        }
        return orderitem;
    }

}
