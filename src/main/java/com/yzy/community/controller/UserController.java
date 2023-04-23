package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ErrorCode;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.contant.RedisConst;
import com.yzy.community.exception.BusinessException;
import com.yzy.community.model.dto.user.UserLoginRequest;
import com.yzy.community.model.dto.user.UserRegisterRequest;
import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.UserVO;
import com.yzy.community.service.FollowService;
import com.yzy.community.service.LikeService;
import com.yzy.community.service.UserService;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController implements CommonConstant {

    @Resource
    private UserService userService;

    @Resource
    private FollowService followService;
    @Resource
    private LikeService likeService;

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userName = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(userName, password)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请完整填写参数");
        }

        return userService.userLogin(userName, password, request);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String userName = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        if (StringUtils.isAnyBlank(userName, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请完整填写星系");
        }
        long result = userService.userRegister(userName, userPassword, checkPassword, email);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户主页
     *
     * @param userId
     * @return
     */
    @GetMapping("/profile/{userId}")
    public BaseResponse<UserVO> getUserProfile(@PathVariable("userId") Integer userId) {
        User user = userService.getById(userId);
        Optional.ofNullable(user).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在"));
        Integer likeCount = likeService.getLikeCount(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setLikeCount(likeCount);


        //关注信息

        //粉丝
        Integer followerCount = followService.findFollowerCount(userId, ENTITY_TYPE_USER);
        //关注数
        Integer followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        userVO.setFollowerCount(followerCount);
        userVO.setFolloweeCount(followeeCount);

        //是否关注
        Integer curUserId = UserHolder.getUser().getId();
        boolean hasFollowed = followService.hasFollowed(curUserId, ENTITY_TYPE_USER, userId);

        userVO.setHasFollowed(hasFollowed);
        return ResultUtils.success(userVO);
    }
}
