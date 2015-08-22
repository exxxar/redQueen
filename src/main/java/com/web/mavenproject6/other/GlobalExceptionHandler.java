/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.other;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

       
    @ExceptionHandler(Exception.class)
    public ModelAndView handleIOException(HttpServletRequest req, Map<String, Object> model, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("thy/error/exception");
        return mav;
    }

    @ExceptionHandler(ArithmeticException.class)
    public ModelAndView handleIOException2(HttpServletRequest request, Exception ex, Map<String, Object> model) {
        logger.error("IOException handler executed");
        model.put("message", ex.toString());
        request.setAttribute("message", ex.toString());
        return new ModelAndView("thy/error/exception");
    }
}
