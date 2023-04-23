package com.yzy.community.model.dto.user;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinitions;

/**
 * @author: yzy
 **/
@Data
public class UserLoginRequest {
    private String username;


    private String password;
}
