package com.yzy.community.service.impl;

import com.yzy.community.contant.RedisConst;
import com.yzy.community.service.LikeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@Service
public class LikeServiceImpl implements LikeService, RedisConst {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Integer getLikeCount(Integer userId) {
        String CommentKey = LIKED_KEY + COMMENT;
        String PostKey = LIKED_KEY + POST;
        Integer commentCount = stringRedisTemplate.opsForSet().members(CommentKey + userId).size();
        Integer postCount = stringRedisTemplate.opsForSet().members(PostKey + userId).size();
        return commentCount + postCount;

    }
}
