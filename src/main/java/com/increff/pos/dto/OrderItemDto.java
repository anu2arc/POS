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
    private ProductService productService;

    public String add(List<OrderItemForm> orderItemForms,int orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos=new ArrayList<>();
        for(OrderItemForm itemForm:orderItemForms){
            ProductPojo productPojo=productService.getByBarcode(itemForm.getBarcode());
            orderItemPojos.add(DtoHelper.convert(itemForm,orderId,productPojo.getId()));
        }
        orderItemService.add(orderItemPojos);
        return "Order placed with ID: "+orderId;
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
