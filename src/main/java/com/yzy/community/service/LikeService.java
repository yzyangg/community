package com.yzy.community.service;

public interface LikeService {

    /**
     * 查询用户收到的点赞数
     *
     * @param userId
     * @return
     */

    Integer getLikeCount(Integer userId);
}
