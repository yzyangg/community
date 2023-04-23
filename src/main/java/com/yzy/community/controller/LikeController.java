package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.contant.RedisConst;
import com.yzy.community.event.EventProducer;
import com.yzy.community.model.entity.Comment;
import com.yzy.community.model.entity.Event;
import com.yzy.community.service.CommentService;
import com.yzy.community.service.PostService;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/like")
@Slf4j
public class LikeController implements RedisConst, CommonConstant {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private EventProducer eventProducer;

    @Resource
    private CommentService commentService;

    @Resource
    private PostService postService;

    /**
     * 点赞
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @GetMapping("")
    public BaseResponse<Boolean> like(Integer entityType, Integer entityId) {
        Integer userId = UserHolder.getUser().getId();
        String redisKey = RedisConst.LIKED_KEY + ((entityType == 1 ? "post" : "comment") + ":") + entityId;
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(redisKey, userId.toString());

        //帖子所对应的用户id
        Integer entityUserId = postService.getById(entityId).getUserId();

        if (isMember) {
            stringRedisTemplate.opsForSet().remove(redisKey, userId.toString());
        } else {
            stringRedisTemplate.opsForSet().add(redisKey, userId.toString());
        }
        String msg = "";
        if (isMember) {
            msg = "取消点赞成功";
        } else {
            msg = "点赞成功";
        }

        //取消点赞不用发消息
        if (!isMember) {
            //出发点赞事件
            Event event = new Event().setTopic(TOPIC_LIKE)
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setUserId(userId)
                    .setData("postId", entityId)
                    .setEntityUserId(entityUserId);

            //发布事件
            eventProducer.fireEvent(event);
        }
        return ResultUtils.success(true, msg);
    }

    /**
     * 查询实体点赞数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @GetMapping("/count")
    public BaseResponse<Integer> count(Integer entityType, Integer entityId) {
        String redisKey = RedisConst.LIKED_KEY + ((entityType == 1 ? "post" : "comment") + ":") + entityId;
        Integer count = Objects.requireNonNull(stringRedisTemplate.opsForSet().size(redisKey)).intValue();
        return ResultUtils.success(count, "查询成功");
    }

    /**
     * 查询某人对实体的点赞状态
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @GetMapping("/status")
    public BaseResponse<Boolean> status(Integer entityType, Integer entityId) {
        Integer userId = UserHolder.getUser().getId();
        String redisKey = RedisConst.LIKED_KEY + ((entityType == 1 ? "post" : "comment") + ":") + entityId;
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(redisKey, userId.toString());
        return ResultUtils.success(isMember, "查询成功");
    }


}
