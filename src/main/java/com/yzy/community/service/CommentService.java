package com.yzy.community.service;

import com.yzy.community.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【comment】的数据库操作Service
 * @createDate 2023-04-17 15:28:20
 */
public interface CommentService extends IService<Comment> {
    /**
     * 根据实体类型和实体id获取评论列表
     *
     * @param entityType
     * @param entityId
     * @return
     */
    List<Comment> getCommentByEntityId(Integer entityType, Long entityId);

    /**
     * 根据实体类型和实体id获取评论数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    Integer getCommentCountByEntityId(Integer entityType, Long entityId);

    Boolean addComment(Integer entityId, Integer entityType, Integer targetId, String content);
}
