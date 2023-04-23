package com.yzy.community.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yzy.community.model.entity.User;
import com.yzy.community.utils.JWTUtil;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.yzy.community.contant.RedisConst.LOGIN_USER_KEY;


/**
 * 刷新登陆状态
 *
 * @author: yzy
 **/
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            return true;
        }
        DecodedJWT decodedJWT = JWTUtil.parseToken(token);
        Integer userId = Integer.parseInt(decodedJWT.getClaim("userId").asString());


        // 2.基于token获取redis中的用户
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);

        if (userMap.isEmpty()) {
            return true;
        }

        //保存到ThreadLocal
        User user = BeanUtil.fillBeanWithMap(userMap, new User(), false);

        //每个请求夺都需要在这里设置当前登录用户！！！！！！！！！
        UserHolder.setUser(user);
        // TODO 测试取消该功能
        // stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //整个请求执行完执行
        UserHolder.clear();
        log.info("退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了退出了?");
    }
}
