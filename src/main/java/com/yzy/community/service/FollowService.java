package com.yzy.community.service;

import com.yzy.community.model.vo.UserVO;

import java.util.List;

/**
 * 关注服务
 *
 * @author : yzy
 */
public interface FollowService {


    /**
     * 关注
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     */
    void follow(Integer userId, Integer entityType, Integer entityId);

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    void unfollow(Integer userId, Integer entityType, Integer entityId);


    /**
     * 查询某个用户关注的实体的数量
     *
     * @param userId
     * @param entityType
     * @return
     */
    Integer findFolloweeCount(Integer userId, Integer entityType);

    /**
     * 查询某个实体的粉丝数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    Integer findFollowerCount(Integer entityType, Integer entityId);


    /**
     * 查询当前用户是否已关注某实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean hasFollowed(Integer userId, Integer entityType, Integer entityId);

    /**
     * 查询关注的人
     *
     * @param userId
     * @return
     */
    List<UserVO> findFollowees(Integer userId);


    /**
     * 查询粉丝
     *
     * @param userId
     * @return
     */
    List<UserVO> findFollowers(Integer userId);
}
