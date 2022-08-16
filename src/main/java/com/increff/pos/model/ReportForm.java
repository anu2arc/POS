package com.increff.pos.model;

import com.increff.pos.service.ApiException;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ReportForm {
    ZonedDateTime startDate;
    ZonedDateTime endDate;
    String brand;
    String category;
    public void setStartDate(String start) throws ApiException {
        try{
            this.startDate = ZonedDateTime.parse(start);
        }catch (Throwable e){
            throw new ApiException("Invalid date time format must be of ZonedDateTime");
        }
    }
    public void setEndDate(String end) throws ApiException {
        try{
            this.endDate = ZonedDateTime.parse(end);
        }catch (Throwable e){
            throw new ApiException("Invalid date time format must be of ZonedDateTime");
        }
    }
}
