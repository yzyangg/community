package com.yzy.community.service.impl;

import com.yzy.community.contant.CommonConstant;
import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.UserVO;
import com.yzy.community.service.FollowService;
import com.yzy.community.service.LikeService;
import com.yzy.community.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static com.yzy.community.utils.RedisUtil.getFolloweeKey;
import static com.yzy.community.utils.RedisUtil.getFollowerKey;

/**
 * 关注服务
 *
 * @author: yzy
 **/
@Service
public class FollowServiceImpl implements FollowService, CommonConstant {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {
        //开启redis事务
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                String followeeKey = getFolloweeKey(userId, entityType);
                String followerKey = getFollowerKey(entityType, entityId);
                stringRedisTemplate.opsForZSet().add(followeeKey, String.valueOf(entityId), System.currentTimeMillis());
                stringRedisTemplate.opsForZSet().add(followerKey, String.valueOf(userId), System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    @Override
    public void unfollow(Integer userId, Integer entityType, Integer entityId) {
        //开启redis事务
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                String followeeKey = getFolloweeKey(userId, entityType);
                String followerKey = getFollowerKey(entityType, entityId);
                stringRedisTemplate.opsForZSet().remove(followeeKey, String.valueOf(entityId));
                stringRedisTemplate.opsForZSet().remove(followerKey, String.valueOf(userId));
                return operations.exec();
            }
        });
    }

    @Override
    public Integer findFolloweeCount(Integer userId, Integer entityId) {
        String followeeKey = getFolloweeKey(userId, entityId);
        return stringRedisTemplate.opsForZSet().zCard(followeeKey).intValue();
    }

    @Override
    public Integer findFollowerCount(Integer entityType, Integer entityId) {
        String followerKey = getFollowerKey(entityType, entityId);
        return stringRedisTemplate.opsForZSet().zCard(followerKey).intValue();
    }

    @Override
    public boolean hasFollowed(Integer userId, Integer entityType, Integer entityId) {
        String followeeKey = getFolloweeKey(userId, entityType);
        return stringRedisTemplate.opsForZSet().score(followeeKey, String.valueOf(entityId)) != null;
    }

    @Override
    public List<UserVO> findFollowees(Integer userId) {
        //查询关注的人
        Set<String> range = stringRedisTemplate.opsForZSet().range(getFolloweeKey(userId, ENTITY_TYPE_USER), 0, -1);
        assert range != null;
        List<Integer> ids = range.stream().map(Integer::parseInt).collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> users = userService.listByIds(ids);
        List<UserVO> userVOS = users.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            //查询该用户收到的点赞数和关注数
            Integer likeCount = likeService.getLikeCount(user.getId());
            Integer followeeCount = findFolloweeCount(user.getId(), ENTITY_TYPE_USER);
            Integer followerCount = findFollowerCount(ENTITY_TYPE_USER, user.getId());
            userVO.setLikeCount(likeCount);
            userVO.setFolloweeCount(followeeCount);
            userVO.setFollowerCount(followerCount);
            Double score = stringRedisTemplate.opsForZSet().score(getFolloweeKey(userId, ENTITY_TYPE_USER), String.valueOf(user.getId()));
            userVO.setFollowTime(new Date(score.longValue()));
            userVO.setHasFollowed(true);
            return userVO;
        }).collect(Collectors.toList());
        return userVOS;
    }

    @Override
    public List<UserVO> findFollowers(Integer userId) {

        //查询关注我的人
        String followerKey = getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<String> range = stringRedisTemplate.opsForZSet().range(followerKey, 0, -1);

        assert range != null;
        List<Integer> ids = range.stream().map(Integer::parseInt).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<User> users = userService.listByIds(ids);
        List<UserVO> userVOS = users.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            //查询该用户收到的点赞数和关注数
            Integer likeCount = likeService.getLikeCount(user.getId());
            Integer followeeCount = findFolloweeCount(user.getId(), ENTITY_TYPE_USER);
            Integer followerCount = findFollowerCount(ENTITY_TYPE_USER, user.getId());
            userVO.setLikeCount(likeCount);
            userVO.setFolloweeCount(followeeCount);
            userVO.setFollowerCount(followerCount);
            Double score = stringRedisTemplate.opsForZSet().score(getFollowerKey(ENTITY_TYPE_USER, userId), String.valueOf(user.getId()));
            userVO.setFollowTime(new Date(score.longValue()));
            return userVO;
        }).collect(Collectors.toList());
        return userVOS;
    }
}
