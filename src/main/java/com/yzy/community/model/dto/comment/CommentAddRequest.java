package com.yzy.community.model.dto.comment;

import lombok.Data;

/**
 * @author: yzy
 **/
@Data
public class CommentAddRequest {

    private Integer entityType;
    private Integer entityId;
    private Integer targetId;
    private String content;
}
