package com.yzy.community.config;


import com.yzy.community.aop.LoginInterceptor;
import com.yzy.community.aop.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                //拦截所有页面，刷新token
                .addPathPatterns("/**").order(0);
        registry.addInterceptor(new LoginInterceptor())
                //需要登录登陆界面
                .excludePathPatterns("/user/login", "/user/register").order(1);
    }
}
