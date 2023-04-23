package com.yzy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ErrorCode;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.exception.BusinessException;
import com.yzy.community.model.entity.User;
import com.yzy.community.service.UserService;
import com.yzy.community.mapper.UserMapper;
import com.yzy.community.utils.JWTUtil;
import com.yzy.community.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzy.community.contant.RedisConst.LOGIN_USER_KEY;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-04-17 15:28:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    public static final String SALT = "yzy";
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BaseResponse userLogin(String userName, String password, HttpServletRequest request) {


        if (UserHolder.getUser() != null) {
            throw new BusinessException(ErrorCode.SIMPLE_ERROR, "用户已登录");
        }
        //校验所有参数
        if (userName == null || userName.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password == null || password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        //校验账号或者邮箱是否已经存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userName);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }

        //密码加盐加密
        String encryptPassword = DigestUtils.md5DigestAsHex((password + SALT).getBytes());
        if (!user.getPassword().equals(encryptPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }


        //TODO 脱敏
        //hash存储
        User userCopy = new User(user);
        userCopy.setCreateTime(null);
        Map<String, String> userMap = new DecoratingStringHashMapper<>(new Jackson2HashMapper(true)).toHash(userCopy);

        //生成token

        //prefix + token + userId
        Map<String, String> map = new HashMap<>();
        map.put("userId", user.getId().toString());
        String token = JWTUtil.genToken(map);

        String redisKey = LOGIN_USER_KEY + token;



        //保存并设置过期时间
        stringRedisTemplate.opsForHash().putAll(redisKey, userMap);

        return ResultUtils.success(token);
    }

    @Override
    public long userRegister(String userName, String password, String checkPassword, String email) {
        //校验所有参数
        if (userName == null || userName.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password == null || password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        if (email != null) {
            String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
            }
        }
        //校验账号或者邮箱是否已经存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userName);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已存在");
        }
        //校验邮箱是否已经存在
        if (email != null) {
            LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(User::getEmail, email);
            long count1 = this.count(wrapper1);
            if (count1 > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已存在");
            }
        }
        //注册用户
        User user = new User();

        //TODO 通过邮件发送激活码，激活账号
        //密码加盐加密
        String encryptPassword = DigestUtils.md5DigestAsHex((password + SALT).getBytes());
        user.setUsername(userName).setPassword(encryptPassword).setEmail(email);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        User currentUser = UserHolder.getUser();
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        return user;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (UserHolder.getUser() == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "未登录");
        }
        String token = request.getHeader("Authorization");
        String redisKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.delete(redisKey);
        UserHolder.clear();
        return true;
    }
}




