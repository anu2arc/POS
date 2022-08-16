package com.increff.pos.controller;

import com.google.gson.Gson;
import com.increff.pos.service.ApiException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@ControllerAdvice
public class RestControllerAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public void handleBindNumberFormatException(HttpServletRequest request, HttpServletResponse response,MethodArgumentTypeMismatchException e) throws IOException {
        JSONObject obj=new JSONObject();
        System.out.println(e.getStackTrace());
        System.out.println(e.getMessage());
        System.out.println(e.getLocalizedMessage());
        obj.put("message", "Bad Request");
        obj.put("description", "Invalid format");
        obj.put("code", 400);
        String json = new Gson().toJson(obj);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ApiException.class, HttpMessageNotReadableException.class,Exception.class})
    @ResponseBody
    public void handleBindException(HttpServletRequest request, HttpServletResponse response,Exception e) throws IOException {
        JSONObject obj=new JSONObject();
        System.out.println(e.getStackTrace());
        System.out.println(e.getMessage());
        System.out.println(e.getLocalizedMessage());
        obj.put("message", "Bad Request");
        obj.put("description", e.getMessage());
        obj.put("code", 400);
        String json = new Gson().toJson(obj);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}