package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.event.EventProducer;
import com.yzy.community.model.dto.comment.CommentAddRequest;
import com.yzy.community.model.entity.Event;
import com.yzy.community.service.CommentService;
import com.yzy.community.service.PostService;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController implements CommonConstant {


    @Resource
    private CommentService commentService;


    @Resource
    private EventProducer eventProducer;
    @Resource
    private PostService postService;

    /**
     * 发表评论
     *
     * @param commentAddRequest
     * @return
     */
    @PostMapping("/add")
    @Transactional
    public BaseResponse<Boolean> addComment(@RequestBody CommentAddRequest commentAddRequest) {

        Integer entityId = commentAddRequest.getEntityId();
        Integer entityType = commentAddRequest.getEntityType();
        Integer targetId = commentAddRequest.getTargetId();
        String content = commentAddRequest.getContent();
        Boolean aBoolean = commentService.addComment(entityId, entityType, targetId, content);
        postService.addComment(entityId);


        //出发评论事件
        Event event = new Event().setTopic(TOPIC_COMMENT)
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setUserId(UserHolder.getUser().getId())
                .setEntityUserId(targetId)
                .setData("postId", entityId);

        //设置被评论人id
        if (entityType == ENTITY_TYPE_POST) {
            event.setEntityUserId(postService.getById(entityId).getUserId());
        } else if (entityType == ENTITY_TYPE_COMMENT) {
            event.setEntityUserId(commentService.getById(entityId).getUserId());
        }

        //发布事件
        eventProducer.fireEvent(event);
        return ResultUtils.success(aBoolean, "评论成功");
    }
}
