package com.yzy.community.model.vo;

import com.yzy.community.model.entity.Comment;
import com.yzy.community.model.entity.Post;
import lombok.Data;

import java.util.List;

/**
 * @author: yzy
 **/
@Data
public class PostVO extends Post {
    private String authorName;
    private List<Comment> comments;
}
