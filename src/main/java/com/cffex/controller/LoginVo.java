package com.cffex.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginVo {
    @NotBlank(message="username can't be null")
    @Pattern(regexp="^[a-zA-Z0-9]*$",message="请使用字母或数字！")
    private String username;
    @NotBlank(message="password can't be null")
    private String password;

}
