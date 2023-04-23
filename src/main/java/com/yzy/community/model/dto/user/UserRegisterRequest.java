package com.yzy.community.model.dto.user;

import lombok.Data;

/**
 *  用户注册请求
 */
@Data
public class UserRegisterRequest {
    private String username;

    private String password;

    private String checkPassword;
    private String email;
}
