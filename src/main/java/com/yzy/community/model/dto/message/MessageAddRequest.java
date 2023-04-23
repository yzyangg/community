package com.yzy.community.model.dto.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yzy.community.model.entity.Message;
import lombok.Data;

import java.util.Date;

/**
 * @author: yzy
 **/
@Data
public class MessageAddRequest {


    /**
     * 发送者id
     */
    private Integer fromId;

    /**
     * 接收者id
     */
    private Integer toId;


    /**
     * 内容
     */
    private String content;


}
