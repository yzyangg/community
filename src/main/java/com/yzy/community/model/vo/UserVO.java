package com.yzy.community.model.vo;

import com.yzy.community.model.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * @author: yzy
 **/
@Data
public class UserVO extends User {
    /**
     * 收到的点赞数
     */
    private Integer likeCount;
    /**
     * 关注的人数
     */
    private Integer followeeCount;
    /**
     * 粉丝数
     */
    private Integer followerCount;
    /**
     * 关注时间
     */
    private Date followTime;
    /**
     * 是否已关注
     */
    private boolean hasFollowed;

}
