package com.yzy.community.model.dto.post;

import lombok.Data;

/**
 * @author: yzy
 **/
@Data
public class PostAddRequest {
    private Integer userId;
    private String title;
    private String content;

}
