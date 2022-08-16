package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
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
    private OrderDto dto;

    @ApiOperation(value = "Deletes an order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets an order by ID")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer id) throws ApiException {
        return dto.getById(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "list of orders for a date range")
    @RequestMapping(path = "/api/order/range", method = RequestMethod.POST)
    public List<OrderData> getByRange(@RequestBody OrderForm f) throws ApiException {
        return dto.getByRange(f);
    }
}
