package com.yzy.community.mapper;

import cn.hutool.core.date.chinese.LunarFestival;
import com.yzy.community.model.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【message】的数据库操作Mapper
 * @createDate 2023-04-17 15:28:29
 * @Entity com.yzy.community.model.entity.Message
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> getConversation(Integer userId);

    Integer getConversionCount(Integer userId);

    Message getLatestNotice(Integer userId, String topic);

    Integer getNoticeCount(Integer userId, String topic);

    Integer getNoticeUnreadCount(Integer userId, String topic);
}




