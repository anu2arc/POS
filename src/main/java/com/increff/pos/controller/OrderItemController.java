package com.increff.pos.controller;


import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.Data.OrderItemData;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private InvoiceService service;
    @ApiOperation(value = "place order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String add(@RequestBody List<OrderItemForm> form) throws Exception {
        return orderItemDto.add(form);
    }
    @ApiOperation(value = "Gets all items by orderId")
    @RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable Integer orderId) throws ApiException {
        return orderItemDto.getOrder(orderId);
    }
    @ApiOperation(value = "Gets invoice for an orderID")
    @RequestMapping(path = "/invoice/{orderId}", method = RequestMethod.GET)
    public void getInvoice(HttpServletResponse response,@PathVariable Integer orderId) throws ApiException, IOException, TransformerException {
        service.getOrderInvoice(response,orderId);
    }
}
