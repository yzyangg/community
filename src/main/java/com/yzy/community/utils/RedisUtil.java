package com.yzy.community.utils;

import com.yzy.community.contant.CommonConstant;
import com.yzy.community.contant.RedisConst;

/**
 * redis工具类
 *
 * @author: yzy
 **/
public class RedisUtil implements RedisConst, CommonConstant {

    /**
     * 获取某个用户关注的实体的key(通过type查询，比如关注的帖子，关注的用户)
     * redis中以userId+entityType作为key value则为具体的实体id
     *
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(Integer userId, Integer entityType) {
        return FOLLOWEE_KEY + userId + ":" + entityType;
    }

    /**
     * 获取某个实体的粉丝的key
     *
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(Integer entityType, Integer entityId) {
        return FOLLOWER_KEY + entityType + ":" + entityId;
    }

    /**
     * 单日UV
     *
     * @param date
     * @return
     */
    public static String getUVKey(String date) {
        return PREFIX_UV + date;
    }

    /**
     * 区间UV
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + startDate + ":" + endDate;

    }

    /**
     * 单日活跃用户
     *
     * @param date
     * @return
     */
    public static String getDAUKey(String date) {
        return PREFIX_DAU + date;
    }

    /**
     * 区间活跃用户
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + startDate + ":" + endDate;
    }
}

