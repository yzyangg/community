package com.yzy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.model.entity.Message;
import com.yzy.community.service.MessageService;
import com.yzy.community.mapper.MessageMapper;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2023-04-17 15:28:29
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService, CommonConstant {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public List<Message> getConversationList(Integer userId) {
        List<Message> conversations = messageMapper.getConversation(userId);
        return conversations;
    }

    @Override
    public Integer getConversionCount(Integer userId) {
        Integer conversionCount = messageMapper.getConversionCount(userId);
        return conversionCount;
    }

    @Override
    public List<Message> getLetter(String conversationId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getConversationId, conversationId);
        wrapper.ne(Message::getStatus, 2);
        wrapper.ne(Message::getFromId, 1);
        List<Message> messages = list(wrapper);
        return messages;
    }

    @Override
    public int getLettersCount(String conversationId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getConversationId, conversationId);
        wrapper.ne(Message::getStatus, 2);
        wrapper.ne(Message::getFromId, 1);
        Long count = count(wrapper);
        return count.intValue();
    }

    @Override
    public int getLettersUnreadCount(Integer userId, String conversationId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getConversationId, conversationId);
        wrapper.eq(Message::getToId, userId);
        wrapper.ne(Message::getFromId, 1);
        wrapper.eq(Message::getStatus, 0);
        Long count = count(wrapper);
        return count.intValue();
    }

    @Override
    public boolean addMessage(Integer fromId, Integer toId, String content, String conversationId) {
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setContent(content);
        message.setConversationId(conversationId);
        boolean success = save(message);
        return success;


    }

    @Override
    public boolean updateStatus(List<Integer> ids, Integer status) {
        try {
            listByIds(ids).stream().map(message -> {
                message.setStatus(status);
                return message;
            }).forEach(this::updateById);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public Message getLatestNotice(Integer userId, String topic) {
        Message latestNotice = messageMapper.getLatestNotice(userId, topic);
        return latestNotice;
    }

    @Override
    public int getNoticeCount(Integer userId, String topic) {
        Integer noticeCount = messageMapper.getNoticeCount(userId, topic);
        return noticeCount;
    }

    @Override
    public int getNoticeUnreadCount(Integer userId, String topic) {
        Integer noticeUnreadCount = messageMapper.getNoticeUnreadCount(userId, topic);
        return noticeUnreadCount;
    }


    @Override
    public List<Message> getNoticeList(Integer userId, String topic) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getFromId, SYSTEM_USER_ID);
        wrapper.eq(Message::getToId, userId);
        wrapper.eq(Message::getConversationId, topic);
        wrapper.orderByDesc(Message::getCreateTime);
        List<Message> messages = list(wrapper);
        return messages;
    }
}




