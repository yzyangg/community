package com.yzy.community.model.vo;

import com.yzy.community.model.entity.Message;
import lombok.Data;

/**
 * @author: yzy
 **/
@Data

public class MessageVO extends Message {
    /**
     * 对方头像
     */
    private String fromUserAvatar;
    /**
     * 私信数
     */
    private Integer letterCount;
    /**
     * 未读私信数
     */
    private Integer unreadCount;
}
