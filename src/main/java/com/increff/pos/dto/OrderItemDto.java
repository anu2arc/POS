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
        ProductPojo productPojo=null;
        try{
            productPojo=productService.getByBarcode(orderItem.getBarcode());
        }
        catch (ApiException apiException){
            throw new ApiException("Invalid barcode :"+orderItem.getBarcode());
        }
        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());
        if(inventoryPojo.getQuantity()< orderItem.getQuantity())
            throw new ApiException("Max Quantity for product "+orderItem.getBarcode()+" is :"+inventoryPojo.getQuantity());
        if(orderItem.getSellingprice()> productPojo.getMrp())
            throw new ApiException("Selling price cannot be more than MRP for Product :"+orderItem.getBarcode());
    }
    public String add(List<OrderItemForm> orderItemForms) throws ApiException {
        StringBuilder response=new StringBuilder();
        HashMap<String,OrderItemPojo> list=new HashMap<>(); // todo use set
        OrderPojo orderPojo=orderService.add();
        int rowNo=0;
        for(OrderItemForm orderItem:orderItemForms) {
            rowNo++;
            try{
                orderItemUtil.validate(orderItem);
                check(orderItem);
                if(list.containsKey(orderItem.getBarcode()))
                    throw new ApiException((rowNo)+": Duplicate Product Present");// todo change message
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
