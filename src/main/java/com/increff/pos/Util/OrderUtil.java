package com.increff.pos.Util;

import com.increff.pos.service.ApiException;

import java.time.ZonedDateTime;

public class OrderUtil {
    public ZonedDateTime convert(String date) throws ApiException {
        try{
            return ZonedDateTime.parse(date);
        }catch (Throwable e){
            throw new ApiException("Invalid date time format must be of ZonedDateTime");
        }
    }
    public void validate(ZonedDateTime start,ZonedDateTime end) throws ApiException {
     if(start.isAfter(end))
         throw new ApiException("Start Date cannot be greater then End Date");
     if(start.isAfter(ZonedDateTime.now()))
         throw new ApiException("Start Date cannot be greater then Today's Date");
    }
}
