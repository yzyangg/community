package com.yzy.community.service;

import com.yzy.community.model.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzy.community.model.vo.PostVO;

import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【post】的数据库操作Service
 * @createDate 2023-04-17 15:28:32
 */
public interface PostService extends IService<Post> {

    PostVO getPostVoById(Long id);

    Boolean addComment(Integer entityId);

    /**
     * 通过mysql搜索帖子数据
     *
     * @param keyword
     * @return
     */
    List<Post> searchPostByKeywordUsingMysql(String keyword);
}
