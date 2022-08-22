package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.Data.OrderData;
import com.increff.pos.model.Form.OrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Gets an order by ID")
    @RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getById(orderId);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "list of orders for a date range")
    @RequestMapping(path = "/api/order/range", method = RequestMethod.POST)
    public List<OrderData> getByRange(@RequestBody OrderForm f) throws ApiException {
        return orderDto.getByRange(f);
    }
}
