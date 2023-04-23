package com.yzy.community.service;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-17 15:28:34
*/
public interface UserService extends IService<User> {

    BaseResponse userLogin(String userName, String password, HttpServletRequest request);

    long userRegister(String userName, String password, String checkPassword, String email);


    User getLoginUser(HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);
}
