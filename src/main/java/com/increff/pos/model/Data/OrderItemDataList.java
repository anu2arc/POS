package com.increff.pos.model.Data;

import com.increff.pos.pojo.OrderItemPojo;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class OrderItemDataList {
    private Integer orderId;
    private Double total;
    private String time;
    private List<OrderItemPojo> orderItems;

    public OrderItemDataList(){
    }
    public OrderItemDataList(List<OrderItemPojo> orderItemPojoList, ZonedDateTime time, Double total, Integer orderId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        this.time=time.format(formatter);
        this.total=total;
        this.orderId=orderId;
        orderItems =orderItemPojoList;
    }
}
