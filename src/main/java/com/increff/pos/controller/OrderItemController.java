package com.increff.pos.controller;


import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
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
public class OrderItemController {

    @Autowired
    private OrderItemDto dto;

    @Autowired
    private InvoiceService service;
    @ApiOperation(value = "place order")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public List<String> add(@RequestBody List<OrderItemForm> form) throws Exception {
        return dto.add(form);
    }

    @ApiOperation(value = "Gets an product by ID")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable Integer id) throws ApiException {
        return dto.getOrder(id);
    }

    @ApiOperation(value = "Gets an product by ID")
    @RequestMapping(path = "/api/orderitem/invoice/{id}", method = RequestMethod.GET)
    public HttpServletResponse getInvoice(@PathVariable Integer id) throws ApiException, IOException, TransformerException {
        return service.getOrderInvoice(id);
    }
}
