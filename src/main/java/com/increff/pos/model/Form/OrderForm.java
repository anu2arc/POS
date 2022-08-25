package com.increff.pos.model.Form;

import com.increff.pos.service.ApiException;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderForm {
    private String startDate;
    private String endDate;
}
