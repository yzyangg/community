package com.yzy.community.model.dto.message;

import lombok.Data;

import java.util.List;

/**
 * @author: yzy
 **/
@Data
public class MessageUpdateRequest {
    /**
     * 要更改状态的id的集合
     */
    List<Integer> ids;
    /**
     * 状态
     */
    Integer status;


}
