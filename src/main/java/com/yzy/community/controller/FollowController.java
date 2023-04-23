package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ErrorCode;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.event.EventProducer;
import com.yzy.community.exception.BusinessException;
import com.yzy.community.model.entity.Event;
import com.yzy.community.model.vo.UserVO;
import com.yzy.community.service.FollowService;
import com.yzy.community.service.LikeService;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: yzy
 **/


@RestController
@Slf4j
@RequestMapping("/")
public class FollowController implements CommonConstant {
    @Resource
    private FollowService followService;

    @Resource
    private EventProducer eventProducer;

    /**
     * 关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/follow")
    public BaseResponse<Boolean> follow(Integer entityType, Integer entityId) {
        try {
            Integer useId = UserHolder.getUser().getId();
            followService.follow(useId, entityType, entityId);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ACTION_ERROR, "关注失败");
        }
        Event event = new Event();
        event
                .setUserId(UserHolder.getUser().getId())
                .setEntityUserId(entityId)
                .setEntityType(entityType)
                .setTopic(TOPIC_FOLLOW)
                .setEntityUserId(entityId);
        //触发关注事件
        eventProducer.fireEvent(event);


        return ResultUtils.success(true, "关注成功");

    }

    /**
     * 取消关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("/unfollow")
    public BaseResponse<Boolean> unfollow(Integer entityType, Integer entityId) {
        try {
            Integer useId = UserHolder.getUser().getId();
            followService.unfollow(useId, entityType, entityId);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ACTION_ERROR, "取消关注失败");
        }
        return ResultUtils.success(true, "取消关注成功");

    }

    /**
     * 查询user的关注的人
     *
     * @param userId
     * @return
     */
    @GetMapping("/followes")
    public BaseResponse<List<UserVO>> findFollowees(Integer userId) {
        List<UserVO> followees = followService.findFollowees(userId);
        return ResultUtils.success(followees);
    }

    /**
     * 查询user的粉丝
     *
     * @param userId
     * @return
     */
    @GetMapping("/followers")
    public BaseResponse<List<UserVO>> findFollowers(Integer userId) {
        List<UserVO> followers = followService.findFollowers(userId);
        return ResultUtils.success(followers);
    }
}
