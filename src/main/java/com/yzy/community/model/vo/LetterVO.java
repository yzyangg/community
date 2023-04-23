package com.yzy.community.model.vo;

import com.yzy.community.model.entity.Message;
import lombok.Data;

/**
 * @author: yzy
 **/
@Data

public class LetterVO extends Message {
    /**
     * 对方头像
     */
    private String fromUserAvatar;

    private String fromUserName;

}
