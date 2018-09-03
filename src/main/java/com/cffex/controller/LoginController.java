package com.cffex.controller;


import com.cffex.entity.ResponseResult;
import com.cffex.entity.User;
import com.cffex.exception.JsonValidationException;
import com.cffex.exception.ServiceException;
import com.cffex.repository.UserRepository;
import com.cffex.utils.JWTUtils;
import com.cffex.utils.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;


@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JWTUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    Integer maxPerPage=5000;


    @RequestMapping(value = "/register", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces="application/json;charset=UTF-8")
    public ResponseResult register(@RequestBody RegisterVo data, HttpServletRequest request,HttpServletResponse response) {
        ResponseResult result= new ResponseResult();
        validateEntity(data);
        if(!data.getConfirmPassword().equals(data.getPassword())){
            throw new ServiceException(100,"password does not match!");
        }
        if(userRepository.isUsernameTaken(data.getUsername())){
            throw new ServiceException(101,"username is taken!");
        }

        String salt=PasswordEncryptor.generateSalt();

        PasswordEncryptor encryptor= new PasswordEncryptor(salt);

        userRepository.createUser(data.getUsername(),encryptor.encode(data.getPassword()),salt);

        User user= userRepository.findByUserName(data.getUsername());
        result.setData(jwtUtils.generateToken(user));
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces="application/json;charset=UTF-8")
    public ResponseResult login(@RequestBody LoginVo data, HttpServletRequest request,HttpServletResponse response) {
        ResponseResult result= new ResponseResult();
        validateEntity(data);
        User user= userRepository.findByUserName(data.getUsername());
        PasswordEncryptor encryptor= new PasswordEncryptor(user.getSalt());
        if(!encryptor.isPasswordValid(user.getPassword(),data.getPassword())){
            throw new ServiceException(401,"username or password is not correct!");
        }
        result.setData(jwtUtils.generateToken(user));
        return result;
    }



    private void validateEntity(Object entity) {
        Set<ConstraintViolation<Object>> validationResult = validator.validate(entity);
        logger.debug("validate result: {}", validationResult);
        if (validationResult.size() != 0) {
            throw new JsonValidationException(validationResult);
        }
    }

}