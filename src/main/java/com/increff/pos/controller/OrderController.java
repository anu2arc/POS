package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.OrderForm;
import com.increff.pos.model.Form.OrderItemForm;
import com.increff.pos.service.ApiException;
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
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Gets an order by ID")
    @RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getById(orderId);
    }

    @ApiOperation(value = "place order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String add(@RequestBody List<OrderItemForm> form) throws Exception {
        return orderDto.addOrderItem(form);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "list of orders for a date range")
    @RequestMapping(path = "/range", method = RequestMethod.POST)
    public List<OrderData> getByRange(@RequestBody OrderForm f) throws ApiException {
        return orderDto.getByRange(f);
    }

    @ApiOperation(value = "Gets invoice for an orderID")
    @RequestMapping(path = "/invoice/{orderId}", method = RequestMethod.GET)
    public void getInvoice(HttpServletResponse response, @PathVariable Integer orderId) throws ApiException, IOException, TransformerException {
        orderDto.getInvoice(response,orderId);
    }
}
