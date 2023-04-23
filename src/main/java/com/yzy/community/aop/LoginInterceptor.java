package com.yzy.community.aop;


import com.yzy.community.common.ErrorCode;
import com.yzy.community.exception.BusinessException;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: yzy
 **/

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.判断是否需要拦截
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截，设置状态码
           throw  new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        // 有用户，则放行
        return true;
    }
}
