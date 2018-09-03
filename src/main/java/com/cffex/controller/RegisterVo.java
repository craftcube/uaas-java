package com.cffex.controller;

import com.cffex.entity.Comments;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class RegisterVo implements Serializable {

    @NotBlank(message="username can't be null")
    @Pattern(regexp="^[a-zA-Z0-9]*$",message="请使用字母或数字！")
    private String username;
    @NotBlank(message="password can't be null")
    private String password;
    @NotBlank(message="confirmPassword can't be null")
    private String confirmPassword;

}
