
package com.increff.pos.controller;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pos.model.Data.MessageData;
import com.increff.pos.service.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(TypeMismatchException.class)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageData handleTypeMismatch(TypeMismatchException e) {
        MessageData data = new MessageData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public MessageData handleException(HttpMessageConversionException e) {
        MessageData data = new MessageData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable e) {
        MessageData data = new MessageData();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        return data;
    }
}