package com.cffex.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class User {
    private long userId;
    private String username;
    private String password;
    private String salt;
    private Date createdOn;
    private Boolean enabled;
    private List<Role> roles;
}
