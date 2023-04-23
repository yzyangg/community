package com.yzy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.controller.PostController;
import com.yzy.community.mapper.PostMapper;
import com.yzy.community.model.entity.Post;
import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.PostVO;
import com.yzy.community.service.CommentService;
import com.yzy.community.service.PostService;
import com.yzy.community.utils.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lenovo
 * @description 针对表【post】的数据库操作Service实现
 * @createDate 2023-04-17 15:28:32
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService, CommonConstant {

    @Resource
    private CommentService commentService;

    @Override
    public PostVO getPostVoById(Long id) {
        User user = UserHolder.getUser();
        String username = user.getUsername();
        PostVO postVO = new PostVO();
        Post post = getById(id);
        BeanUtils.copyProperties(post, postVO);
        postVO.setAuthorName(username);
        postVO.setCommentCount(commentService.getCommentCountByEntityId(ENTITY_TYPE_POST, id));
        postVO.setComments(commentService.getCommentByEntityId(ENTITY_TYPE_POST, id));
        return postVO;

    }

    @Override
    public Boolean addComment(Integer entityId) {
        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, entityId).setSql("comment_count = comment_count + 1");
        boolean success = update(wrapper);
        return success;
    }
}




