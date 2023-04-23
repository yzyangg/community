package com.yzy.community.service;

import com.yzy.community.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import io.netty.handler.codec.MessageAggregator;
import org.springframework.context.support.MessageSourceSupport;

import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【message】的数据库操作Service
 * @createDate 2023-04-17 15:28:29
 */
public interface MessageService extends IService<Message> {
    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     *
     * @param userId
     * @return
     */
    List<Message> getConversationList(Integer userId);

    /**
     * 查询当前用会话数量
     *
     * @param userId
     * @return
     */
    Integer getConversionCount(Integer userId);

    /**
     * 查询某个会话私信列表
     *
     * @param conversationId
     * @return
     */
    List<Message> getLetter(String conversationId);

    /**
     * 获取私信数
     *
     * @param conversationId
     * @return
     */
    int getLettersCount(String conversationId);

    /**
     * 查询未读私信数量
     *
     * @param userId
     * @param conversationId
     * @return
     */
    int getLettersUnreadCount(Integer userId, String conversationId);

    /**
     * 添加私信
     *
     * @param fromId
     * @param toId
     * @param content
     * @param conversationId
     * @return
     */
    boolean addMessage(Integer fromId, Integer toId, String content, String conversationId);

    /**
     * 修改消息状态
     *
     * @param ids
     * @param status
     * @return
     */
    boolean updateStatus(List<Integer> ids, Integer status);

    /**
     * 查询某个主题下最新的通知
     *
     * @param userId
     * @param topic
     * @return
     */
    Message getLatestNotice(Integer userId, String topic);

    List<Message> getNoticeList(Integer userId, String topic);

    /**
     * 查询某个主题所包含的通知数量
     *
     * @param userId
     * @param topic
     * @return
     */
    int getNoticeCount(Integer userId, String topic);

    /**
     * 查询某个主题所包含的通知列表
     *
     * @param userId
     * @param topic
     * @return
     */
    int getNoticeUnreadCount(Integer userId, String topic);

}
