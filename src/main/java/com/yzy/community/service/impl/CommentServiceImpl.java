package com.yzy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzy.community.model.entity.Comment;
import com.yzy.community.model.entity.User;
import com.yzy.community.service.CommentService;
import com.yzy.community.mapper.CommentMapper;
import com.yzy.community.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Lenovo
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2023-04-17 15:28:20
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {


    @Override
    public List<Comment> getCommentByEntityId(Integer entityType, Long entityId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getEntityType, entityType);
        wrapper.eq(Comment::getEntityId, entityId);
        List<Comment> list = list(wrapper);
        return list;
    }

    @Override
    public Integer getCommentCountByEntityId(Integer entityType, Long entityId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getEntityType, entityType);
        wrapper.eq(Comment::getEntityId, entityId);
        Long count = count(wrapper);
        return count.intValue();
    }

    @Override
    public Boolean addComment(Integer entityId, Integer entityType, Integer targetId, String content) {
        Comment comment = new Comment();
        comment.setEntityId(entityId);
        Integer userId = UserHolder.getUser().getId();
        comment.setUserId(userId);
        comment.setEntityType(entityType);
        comment.setTargetId(targetId);
        comment.setContent(content);
        boolean success = save(comment);
        return success;

    }
}




