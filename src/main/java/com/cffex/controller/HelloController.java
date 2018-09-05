package com.cffex.controller;

import com.cffex.aspect.AuthenticationAspect;
import com.cffex.entity.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    @ResponseBody
    public ResponseResult home(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result= new ResponseResult();
        logger.info("=============hello===========");
        result.setData("Hello, world!");
        return result;
    }
}
