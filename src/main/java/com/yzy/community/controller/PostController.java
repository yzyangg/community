package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ErrorCode;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.event.EventProducer;
import com.yzy.community.exception.BusinessException;
import com.yzy.community.model.dto.post.PostAddRequest;
import com.yzy.community.model.entity.Event;
import com.yzy.community.model.entity.Post;
import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.PostVO;
import com.yzy.community.service.PostService;
import com.yzy.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController implements CommonConstant {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;


    @Resource
    private EventProducer eventProducer;


    /**
     * 发布帖子
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        if (postAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        //TODO 校验
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        boolean result = postService.save(post);
        if (!result) {
            throw new BusinessException(ErrorCode.ACTION_ERROR, "创建失败");
        }
        long newPostId = post.getId();


        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(loginUser.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        return ResultUtils.success(newPostId, "发布成功");
    }

    /**
     * 根据id获取帖子详情
     *
     * @param id
     * @param request
     * @return
     * @Description
     */
    @GetMapping("/{id}")
    public BaseResponse<Post> getPostVoById(@PathVariable("id") Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        PostVO postVO = postService.getPostVoById(id);
        return ResultUtils.success(postVO);
    }


}
