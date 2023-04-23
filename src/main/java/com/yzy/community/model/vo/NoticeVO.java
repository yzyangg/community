package com.yzy.community.model.vo;

import com.yzy.community.model.entity.Message;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: yzy
 **/
@Data
@Accessors(chain = true)
public class NoticeVO {
    //获取评论通知
    List<Message> commentNotice;
    //获取点赞通知
    List<Message> likeNotice;
    //获取关注通知
    List<Message> followNotice;


    //未读消息总数
    int unreadCount;
}
